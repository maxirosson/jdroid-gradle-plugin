package com.jdroid.gradle.android

import com.android.build.gradle.AppPlugin
import com.jdroid.gradle.android.task.CopyApksTask
import com.jdroid.gradle.commons.Version
import org.gradle.api.Project

public class AndroidApplicationGradlePlugin extends AndroidGradlePlugin {

	public void apply(Project project) {
		super.apply(project);

		if (jdroid.getBooleanProp("FIREBASE_PERFORMANCE_MONITORING_ENABLED", true)) {
			project.apply plugin: 'com.google.firebase.firebase-perf'
		}

		project.task('copyApks', type: CopyApksTask)

		if (jdroid.getBooleanProp("RIBBONIZER_ENABLED", true)) {
			project.apply plugin: 'com.github.gfx.ribbonizer'

			project.ribbonizer {
				builder { variant, iconFile ->
					// change ribbon colors by product flavors
					if (variant.buildType.name == "debug") {
						return yellowRibbonFilter(variant, iconFile)
					} else {
						return greenRibbonFilter(variant, iconFile)
					}
				}
			}
		}

		List<String> components = jdroid.getStringListProp("COMPONENTS")
		if (components != null && components.contains("jdroid-android")) {
			Boolean stethoEnabled = jdroid.getBooleanProp("STETHO_ENABLED", false)
			if (stethoEnabled) {
				project.dependencies {
					debugCompile 'com.facebook.stetho:stetho:1.4.2'
					if (components.contains("jdroid-java-okhttp")) {
						debugCompile 'com.facebook.stetho:stetho-okhttp3:1.4.2'
					}
					debugCompile 'com.facebook.stetho:stetho-js-rhino:1.4.2'
				}

				android.defaultConfig {
					jdroid.setBuildConfigBoolean(android.defaultConfig, "STETHO_ENABLED", stethoEnabled)
					jdroid.setBuildConfigBoolean(android.defaultConfig, "JDROID_JAVA_OKHTTP_ENABLED", components.contains("jdroid-java-okhttp"))
				}
			}
		}

		if (jdroid.getBooleanProp("SPLITS_DISABLED", false)) {
			android.splits.abi.enabled = false
			android.splits.density.enabled = false
		}
		if (jdroid.getBooleanProp("PNG_CRUNCHING_DISABLED", false)) {
			android.aaptOptions.cruncherEnabled = false
		}

		android.defaultConfig {
			versionCode project.version.versionCode
			versionName project.version.toString()

			List<String> resConfigsList = jdroid.getStringListProp("DEBUG_RES_CONFIGS")
			if (resConfigsList != null) {
				resConfigs resConfigsList
			} else {
				resConfigsList = jdroid.getStringListProp("RES_CONFIGS")
				if (resConfigsList != null) {
					resConfigs resConfigsList
				}
			}
		}

		if (jdroid.getBooleanProp("APK_FILENAME_OVERRIDE_ENABLED", true)) {
			android.applicationVariants.all { variant ->

				variant.outputs.each { output ->
					def outputFile = output.outputFile
					if (outputFile != null && outputFile.name.endsWith('.apk')) {
						def appName = jdroid.getStringProp('APK_BASE_NAME', project.getProjectDir().getParentFile().name)
						def fileName = outputFile.name.replace('.apk', "-v${versionName}.apk")
						fileName = fileName.replace(project.getProjectDir().name, appName)
						if (variant.buildType.debuggable && variant.name.endsWith("Release")) {
							fileName = fileName.replace("-v", "-DEBUGGABLE-v")
						}
						output.outputFile = new File(outputFile.parent, fileName)
					}
				}
			}
		}

		android.signingConfigs {

			debug {
				storeFile project.file('./debug.keystore')
			}

			if (jdroid.isReleaseBuildTypeEnabled()) {
				release {
					storeFile project.file(jdroid.getStringProp('STORE_FILE', './debug.keystore'))
					storePassword jdroid.getStringProp('STORE_PASSWORD')
					keyAlias jdroid.getStringProp('KEY_ALIAS')
					keyPassword jdroid.getStringProp('KEY_PASSWORD')
				}
			}
		}
	}

	protected Class<? extends AndroidApplicationGradlePluginExtension> getExtensionClass() {
		return AndroidApplicationGradlePluginExtension.class;
	}

	@Override
	protected Version createVersion(String version) {
		return new AndroidVersion(project, version)
	}

	protected void applyAndroidPlugin() {
		project.apply plugin: AppPlugin
	}
}
