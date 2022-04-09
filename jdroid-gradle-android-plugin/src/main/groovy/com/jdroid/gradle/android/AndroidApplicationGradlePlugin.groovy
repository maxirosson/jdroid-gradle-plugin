package com.jdroid.gradle.android

import com.github.konifar.gradle.remover.UnusedResourcesRemoverExtension
import com.jdroid.gradle.android.task.CopyBuildsTask
import com.jdroid.gradle.commons.utils.ListUtils
import org.gradle.api.Action
import org.gradle.api.Project

public class AndroidApplicationGradlePlugin extends AndroidGradlePlugin {

	// https://github.com/facebook/stetho/blob/master/CHANGELOG.md
	private static final String FACEBOOK_STETHO_VERSION = "1.5.1";

	public void apply(Project project) {
		super.apply(project);

		if (propertyResolver.getBooleanProp("FIREBASE_PERFORMANCE_MONITORING_ENABLED", true)) {
			applyPlugin("com.google.firebase.firebase-perf");
		}
		if (propertyResolver.getBooleanProp("FIREBASE_CRASHLYTICS_PLUGIN_ENABLED", true)) {
			applyPlugin("com.google.firebase.crashlytics");
		}
		if (propertyResolver.getBooleanProp("UNUSED_RESOURCES_REMOVER_ENABLED", false)) {
			applyPlugin("com.github.konifar.gradle.unused-resources-remover");
			UnusedResourcesRemoverExtension unusedResourcesRemoverExtension = (UnusedResourcesRemoverExtension) project.getExtensions().getByName("unusedResourcesRemover");
			unusedResourcesRemoverExtension.setExcludeNames(ListUtils.newArrayList("attrs.xml"));
		}

		CopyBuildsTask copyBuildsTask = project.getTasks().create("copyBuilds", CopyBuildsTask.class);
		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				copyBuildsTask.setLogLevel(project.jdroid.getLogLevel());
			}
		});

		Boolean stethoEnabled = propertyResolver.getBooleanProp("STETHO_ENABLED", false);
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
				jdroid.setBuildConfigBoolean(android.defaultConfig, "STETHO_ENABLED", stethoEnabled);
			}
		}

		if (propertyResolver.getBooleanProp("SPLITS_DISABLED", false)) {
			android.getSplits().abi.setEnable(false);
			android.getSplits().density.setEnable(false);
		}

		List<String> resConfigsList = propertyResolver.getStringListProp("DEBUG_RES_CONFIGS");
		if (resConfigsList != null) {
			android.getDefaultConfig().resConfig(resConfigsList);
		} else {
			resConfigsList = propertyResolver.getStringListProp("RES_CONFIGS");
			if (resConfigsList != null) {
				android.getDefaultConfig().resConfig(resConfigsList);
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

		String appName = propertyResolver.getStringProp('APP_BUILD_BASE_NAME', project.getRootProject().getName());
		if (propertyResolver.getBooleanProp("APK_FILENAME_OVERRIDE_ENABLED", true)) {
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
	}

	protected Class<? extends AndroidApplicationGradlePluginExtension> getExtensionClass() {
		return AndroidApplicationGradlePluginExtension.class;
	}

	protected void applyAndroidPlugin() {
		applyPlugin("com.android.application");
	}

	@Override
	protected boolean fetchIsPublicationConfigurationEnabled() {
		return false;
	}
}
