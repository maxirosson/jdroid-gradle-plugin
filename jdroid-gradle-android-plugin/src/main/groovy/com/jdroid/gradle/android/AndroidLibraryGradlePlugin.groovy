package com.jdroid.gradle.android

import com.android.build.gradle.LibraryPlugin
import com.jdroid.gradle.android.task.PrefixVerificationTask
import com.jdroid.gradle.commons.JdroidPom
import com.jdroid.java.collections.Lists
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc

public class AndroidLibraryGradlePlugin extends AndroidGradlePlugin {

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

			if (isSourcesPublicationEnabled) {
				project.android.buildTypes.all { variant ->
					project.task("${variant.name}AndroidSourcesJar", type: Jar) {
						archiveClassifier = 'sources'
						from project.android.sourceSets.main.java.srcDirs, project.android.sourceSets."${variant.name}".java.srcDirs
					}
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

									artifactId variant.name == "debug" ? artifactId + "-debug" : artifactId

									if (isSourcesPublicationEnabled) {
										artifact source: project."${variant.name}AndroidSourcesJar", classifier: "sources"
									}
									if (isJavaDocPublicationEnabled) {
										artifact source: project.androidJavadocsJar, classifier: "javadoc"
									}
									artifact project.tasks.getByName("bundle${variant.name.capitalize()}Aar")

									// Defining configuration names from which dependencies will be taken (debugImplementation or releaseImplementation and implementation)
									List<String> configurationNames = Lists.newArrayList("${variant.name}Implementation", "implementation");

									Action<? super MavenPom> mavenPom = JdroidPom.createMavenPom(project, artifactId, getPackaging(), configurationNames)
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

