package com.jdroid.gradle.android

import com.android.build.api.dsl.extension.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.jdroid.gradle.android.task.PrefixVerificationTask
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

public class AndroidLibraryGradlePlugin extends AndroidGradlePlugin {

	public void apply(Project project) {
		super.apply(project);

		project.ext.PACKAGING = 'aar'

		android.defaultConfig {
			jdroid.setBuildConfigString(android.defaultConfig, "VERSION", project.version);
		}

		if (jdroid.getResourcePrefix() != null) {
			((LibraryExtension)android).setResourcePrefix(jdroid.getResourcePrefix());
		}

		Boolean isOpenSourceEnabled = propertyResolver.getBooleanProp("OPEN_SOURCE_ENABLED", true);
		if (isOpenSourceEnabled) {
			project.task('androidSourcesJar', type: Jar) {
				classifier = 'sources'
				from android.sourceSets.main.java.sourceFiles, android.sourceSets.debug.java.sourceFiles
			}

			project.artifacts {
				archives project.tasks.androidSourcesJar
			}
		}

		if(jdroid.getPublishNonDefault()) {

			// --------------------------------------
			// The publishNonDefaults flag is not working anymore on android gradle plugin v3.0.0,
			// so we need to add the debug & release artifacts manually
			project.task('generateDebugAar', type:  org.gradle.api.tasks.bundling.Zip) {
				destinationDir = project.file("${project.buildDir}/outputs/aar/${project.getName()}-debug.aar")
			}

			project.task('generateReleaseAar', type:  org.gradle.api.tasks.bundling.Zip) {
				destinationDir = project.file("${project.buildDir}/outputs/aar/${project.getName()}-release.aar")
			}

			project.artifacts {
				archives file: project.generateDebugAar.destinationDir, name: 'debug', classifier: "debug", type: 'aar'
				archives file: project.generateReleaseAar.destinationDir, name: 'release', classifier: "release", type: 'aar'
			}
			// --------------------------------------

			// --------------------------------------
			// The default artifact is added to 'default' & 'archives' configurations on android gradle plugin v3.0.0,
			// so we need to remove it
			project.afterEvaluate {
				project.configurations.default.artifacts.removeAll { it.classifier == "" && it.type == "aar" && it.extension == "aar" }
				project.configurations.archives.artifacts.removeAll { it.classifier == "" && it.type == "aar" && it.extension == "aar" }
			}
			// --------------------------------------


		}

		PrefixVerificationTask prefixVerificationTask = project.task('verifyPrefixes', type: PrefixVerificationTask)
		project.tasks.'uploadArchives'.dependsOn 'verifyPrefixes'
		project.tasks.'uploadArchives'.dependsOn 'assembleDebug'
		project.tasks.'uploadArchives'.dependsOn 'assembleRelease'
		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				prefixVerificationTask.setLogLevel(project.jdroid.getLogLevel());
			}
		});
	}

	protected Class<? extends AndroidLibraryGradlePluginExtension> getExtensionClass() {
		return AndroidLibraryGradlePluginExtension.class;
	}

	protected void applyAndroidPlugin() {
		project.apply plugin: LibraryPlugin
	}
}

