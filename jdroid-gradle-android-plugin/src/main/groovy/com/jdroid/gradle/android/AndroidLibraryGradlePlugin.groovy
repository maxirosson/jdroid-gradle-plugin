package com.jdroid.gradle.android

import com.jdroid.gradle.android.task.PrefixVerificationTask
import com.jdroid.gradle.commons.GroovyUtils
import com.jdroid.gradle.commons.utils.ListUtils
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.jetbrains.dokka.gradle.DokkaTask

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
		if (isPublicationConfigurationEnabled) {
			project.tasks.'publish'.dependsOn 'verifyPrefixes'
			if (jdroid.isReleaseBuildTypeEnabled()) {
				project.tasks.'publish'.dependsOn 'assembleDebug'
				project.tasks.'publish'.dependsOn 'assembleRelease'
			} else {
				project.tasks.'publish'.dependsOn 'assemble'
			}
		}
		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				prefixVerificationTask.setLogLevel(project.jdroid.getLogLevel());
			}
		});

		if (isPublicationConfigurationEnabled) {
			if (isJavaDocPublicationEnabled) {
				// KTS
	//			import org.gradle.jvm.tasks.Jar
	//
	//			val dokkaJar by tasks.creating(Jar::class) {
	//				group = JavaBasePlugin.DOCUMENTATION_GROUP
	//				description = "Assembles Kotlin docs with Dokka"
	//				classifier = "javadoc"
	//				from(tasks.dokka)
	//			}

				project.task('dokkaJar', type: Jar) {
					group = JavaBasePlugin.DOCUMENTATION_GROUP
					description = "Assembles Kotlin docs with Dokka"
					archiveClassifier = 'javadoc'
					from project.tasks.withType(DokkaTask.class)
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
									boolean isDebug = variant.name == "debug"
									artifactId isDebug ? artifactId + "-debug" : artifactId

									if (isSourcesPublicationEnabled) {
										artifact source: project."${variant.name}AndroidSourcesJar", classifier: "sources"
									}
									if (isJavaDocPublicationEnabled) {
										artifact source: project.dokkaJar, classifier: "javadoc"
									}
									artifact project.tasks.getByName("bundle${variant.name.capitalize()}Aar")

									// Defining configuration names from which dependencies will be taken (debugImplementation or releaseImplementation and implementation)
									List<String> configurationNames = ListUtils.newArrayList("${variant.name}Implementation", "implementation");

									Action<? super MavenPom> mavenPom = new AndroidJdroidPom(configurationNames, isDebug).createMavenPom(project, jdroid, artifactId, getPackaging())
									pom(mavenPom)
								}
							}

						}
					}
				}
			}
		}

		if (isSigningPublicationEnabled) {
			GroovyUtils.configureSigning(project, project.publishing.publications)
		}
	}

	protected Class<? extends AndroidLibraryGradlePluginExtension> getExtensionClass() {
		return AndroidLibraryGradlePluginExtension.class;
	}

	@Override
	protected String getPackaging() {
		return "aar";
	}

	@Override
	protected void applyAndroidPlugin() {
		applyPlugin("com.android.library");
	}
}

