package com.jdroid.gradle.android

import com.android.build.gradle.LibraryPlugin
import com.jdroid.gradle.android.task.PrefixVerificationTask
import com.jdroid.gradle.commons.JdroidPom
import com.jdroid.gradle.commons.PropertyResolver
import com.jdroid.java.collections.Lists
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc

public class AndroidLibraryGradlePlugin extends AndroidGradlePlugin {

	public Boolean isPublicationConfigurationEnabled;
	public Boolean isSourcesPublicationEnabled;
	public Boolean isSigningPublicationEnabled;
	public Boolean isJavaDocPublicationEnabled;

	public void apply(Project project) {
		super.apply(project);

		android.defaultConfig {
			jdroid.setBuildConfigString(android.defaultConfig, "VERSION", project.version);
		}

		if (jdroid.getResourcePrefix() != null) {
			android.resourcePrefix jdroid.getResourcePrefix()
		}

		PrefixVerificationTask prefixVerificationTask = project.task('verifyPrefixes', type: PrefixVerificationTask)
		project.tasks.'publish'.dependsOn 'verifyPrefixes'
		project.tasks.'publish'.dependsOn 'assembleDebug'
		project.tasks.'publish'.dependsOn 'assembleRelease'
		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				prefixVerificationTask.setLogLevel(project.jdroid.getLogLevel());
			}
		});

		// TODO This is duplicated on BaseGradlePlugin. I don't know why this doesn't work if I remove this
		isPublicationConfigurationEnabled = propertyResolver.getBooleanProp("PUBLICATION_CONFIGURATION_ENABLED", true);
		isSourcesPublicationEnabled = propertyResolver.getBooleanProp("SOURCES_PUBLICATION_ENABLED", true);
		isSigningPublicationEnabled = propertyResolver.getBooleanProp("SIGNING_PUBLICATION_ENABLED", true);
		isJavaDocPublicationEnabled = propertyResolver.getBooleanProp("JAVADOC_PUBLICATION_ENABLED", true);
		String artifactName = propertyResolver.getStringProp("ARTIFACT_ID");
		if (artifactName == null) {
			artifactName =  new PropertyResolver(project.getRootProject()).getStringProp("ARTIFACT_ID");
			if (artifactName == null) {
				artifactName = project.getName();
			}
		}

		if (isPublicationConfigurationEnabled) {
			if (isJavaDocPublicationEnabled) {
				project.task('androidJavadocs', type: Javadoc) {
					source = project.android.sourceSets.main.java.srcDirs
					classpath += project.files(project.android.getBootClasspath().join(File.pathSeparator))
					project.android.libraryVariants.all { variant ->
						if (variant.name == 'release') {
							owner.classpath += variant.javaCompile.classpath
						}
					}
					exclude '**/R.html', '**/R.*.html', '**/index.html'
				}

				project.task('androidJavadocsJar', type: Jar, dependsOn: 'androidJavadocs') {
					archiveClassifier = 'javadoc'
					from project.androidJavadocs.destinationDir
				}
			}

			// TODO Create a task for each variant
			if (isSourcesPublicationEnabled) {
				project.task('androidSourcesJar', type: Jar) {
					archiveClassifier = 'sources'
					from project.android.sourceSets.main.java.srcDirs
				}
			}
			project.afterEvaluate {

				project.publishing {
					publications {

						// Create different publications for every build types (debug and release)
						project.android.buildTypes.all { variant ->

							if (variant.name != "release" || jdroid.isReleaseBuildTypeEnabled()) {
								// Dynamically creating publications name
								"${variant.name}AndroidLibrary"(MavenPublication) {

									artifactId variant.name == 'debug' ? artifactName + '-debug' : artifactName

									if (isSourcesPublicationEnabled) {
										artifact source: project.androidSourcesJar, classifier: 'sources'
									}
									if (isJavaDocPublicationEnabled) {
										artifact source: project.androidJavadocsJar, classifier: 'javadoc'
									}
									artifact project.tasks.getByName("bundle${variant.name.capitalize()}Aar")

									// Defining configuration names from which dependencies will be taken (debugImplementation or releaseImplementation and implementation)
									List<String> configurationNames = Lists.newArrayList("${variant.name}Implementation", "implementation");

									Action<? super MavenPom> mavenPom = JdroidPom.createMavenPom(project, artifactName, getPackaging(), configurationNames)
									pom(mavenPom)
								}
							}

						}
					}
				}
			}
		}

		if (isSigningPublicationEnabled) {
			applyPlugin('signing')
			project.signing {
				required { !project.version.isSnapshot }
				sign project.publishing.publications
			}
		}
	}

	protected Class<? extends AndroidLibraryGradlePluginExtension> getExtensionClass() {
		return AndroidLibraryGradlePluginExtension.class;
	}

	@Override
	protected String getPackaging() {
		return "aar";
	}

	protected void applyAndroidPlugin() {
		project.apply plugin: LibraryPlugin
	}
}

