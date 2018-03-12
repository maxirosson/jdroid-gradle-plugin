package com.jdroid.gradle.android

import com.android.build.gradle.AppPlugin
import com.jdroid.gradle.android.task.CopyApksTask
import com.jdroid.gradle.android.versioning.AndroidVersion
import com.jdroid.gradle.commons.versioning.Version
import org.gradle.api.Action
import org.gradle.api.Project

public class AndroidApplicationGradlePlugin extends AndroidGradlePlugin {

	// https://github.com/facebook/stetho/blob/master/CHANGELOG.md
	private static final String FACEBOOK_STETHO_VERSION = '1.5.0'

	public void apply(Project project) {
		super.apply(project);

		if (propertyResolver.getBooleanProp("FIREBASE_PERFORMANCE_MONITORING_ENABLED", true)) {
			project.apply plugin: 'com.google.firebase.firebase-perf'
		}
		if (propertyResolver.getBooleanProp("FIREBASE_CRASHLYTICS_ENABLED", true)) {
			project.apply plugin: 'io.fabric'
		}

		CopyApksTask copyApksTask = project.task('copyApks', type: CopyApksTask)
		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				copyApksTask.setLogLevel(project.jdroid.getLogLevel());
			}
		});

		if (propertyResolver.getBooleanProp("RIBBONIZER_ENABLED", true)) {
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

		Boolean stethoEnabled = propertyResolver.getBooleanProp("STETHO_ENABLED", false)
		if (stethoEnabled) {
			project.dependencies {
				debugApi 'com.facebook.stetho:stetho:' + FACEBOOK_STETHO_VERSION
				if (propertyResolver.getBooleanProp("STETHO_OKHTTP3_ENABLED", true)) {
					debugApi 'com.facebook.stetho:stetho-okhttp3:' + FACEBOOK_STETHO_VERSION
				}
				if (propertyResolver.getBooleanProp("STETHO_JS_RHINO_ENABLED", true)) {
					debugApi 'com.facebook.stetho:stetho-js-rhino:' + FACEBOOK_STETHO_VERSION
				}
			}

			android.defaultConfig {
				jdroid.setBuildConfigBoolean(android.defaultConfig, "STETHO_ENABLED", stethoEnabled)
			}
		}

		if (propertyResolver.getBooleanProp("SPLITS_DISABLED", false)) {
			android.splits.abi.enabled = false
			android.splits.density.enabled = false
		}

		android.defaultConfig {
			versionCode project.version.versionCode
			versionName project.version.toString()

			List<String> resConfigsList = propertyResolver.getStringListProp("DEBUG_RES_CONFIGS")
			if (resConfigsList != null) {
				resConfigs resConfigsList
			} else {
				resConfigsList = propertyResolver.getStringListProp("RES_CONFIGS")
				if (resConfigsList != null) {
					resConfigs resConfigsList
				}
			}
		}

		if (propertyResolver.getBooleanProp("APK_FILENAME_OVERRIDE_ENABLED", true)) {
			def appName = propertyResolver.getStringProp('APK_BASE_NAME', project.getProjectDir().getParentFile().name)
			android.applicationVariants.all { variant ->
				variant.outputs.all { output ->
					if (outputFileName.endsWith('.apk')) {
						outputFileName = outputFileName.replace('.apk', "-v${versionName}.apk")
						outputFileName = outputFileName.replace(project.getProjectDir().name, appName)
						if (variant.buildType.debuggable && variant.name.toLowerCase().endsWith("release")) {
							outputFileName = outputFileName.replace("-v", "-DEBUGGABLE-v")
						}
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
					storeFile project.file(propertyResolver.getStringProp('STORE_FILE', './debug.keystore'))
					storePassword propertyResolver.getStringProp('STORE_PASSWORD')
					keyAlias propertyResolver.getStringProp('KEY_ALIAS')
					keyPassword propertyResolver.getStringProp('KEY_PASSWORD')
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
