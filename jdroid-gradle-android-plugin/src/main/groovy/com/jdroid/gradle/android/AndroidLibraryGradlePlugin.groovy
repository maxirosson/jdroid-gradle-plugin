package com.jdroid.gradle.android

import com.android.build.gradle.LibraryPlugin
import com.jdroid.gradle.android.task.PrefixVerificationTask
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

public class AndroidLibraryGradlePlugin extends AndroidGradlePlugin {

	public void apply(Project project) {
		super.apply(project);

		project.ext.PACKAGING = 'aar'

		android.defaultConfig {
			jdroid.setBuildConfigString(android.defaultConfig, "VERSION", project.version)
		}

		if (jdroid.getResourcePrefix() != null) {
			android.resourcePrefix jdroid.getResourcePrefix()
		}

		android.publishNonDefault jdroid.getPublishNonDefault()

		Boolean isOpenSourceEnabled = jdroid.getBooleanProp("OPEN_SOURCE_ENABLED", true)
		if (isOpenSourceEnabled) {
			project.task('androidSourcesJar', type: Jar) {
				classifier = 'sources'
				from android.sourceSets.main.java.sourceFiles, android.sourceSets.debug.java.sourceFiles
			}

			project.artifacts {
				archives project.tasks.androidSourcesJar
			}
		}

		project.task('verifyPrefixes', type: PrefixVerificationTask)
		project.tasks.'uploadArchives'.dependsOn 'verifyPrefixes'
	}

	protected Class<? extends AndroidLibraryGradlePluginExtension> getExtensionClass() {
		return AndroidLibraryGradlePluginExtension.class;
	}

	protected void applyAndroidPlugin() {
		project.apply plugin: LibraryPlugin
	}
}

