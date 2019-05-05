package com.jdroid.gradle.android

import com.android.build.gradle.AppPlugin
import com.github.konifar.gradle.remover.UnusedResourcesRemoverExtension
import com.jdroid.gradle.android.task.CopyBuildsTask
import com.jdroid.gradle.android.versioning.AndroidVersion
import com.jdroid.gradle.commons.versioning.Version
import com.jdroid.java.collections.Lists
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
		if (propertyResolver.getBooleanProp("FIREBASE_CRASHLYTICS_ENABLED", true)) {
			applyPlugin("io.fabric");
		}
		if (propertyResolver.getBooleanProp("UNUSED_RESOURCES_REMOVER_ENABLED", false)) {
			applyPlugin("com.github.konifar.gradle.unused-resources-remover");
			UnusedResourcesRemoverExtension unusedResourcesRemoverExtension = (UnusedResourcesRemoverExtension) project.getExtensions().getByName("unusedResourcesRemover");
			unusedResourcesRemoverExtension.setExcludeNames(Lists.newArrayList("attrs.xml"));
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

		android.getDefaultConfig().setVersionCode(((AndroidVersion)project.getVersion()).getVersionCode());
		android.getDefaultConfig().setVersionName(project.getVersion().toString());

		List<String> resConfigsList = propertyResolver.getStringListProp("DEBUG_RES_CONFIGS");
		if (resConfigsList != null) {
			android.getDefaultConfig().resConfig(resConfigsList);
		} else {
			resConfigsList = propertyResolver.getStringListProp("RES_CONFIGS");
			if (resConfigsList != null) {
				android.getDefaultConfig().resConfig(resConfigsList);
			}
		}


		if (propertyResolver.getBooleanProp("APK_FILENAME_OVERRIDE_ENABLED", true)) {
			String appName = propertyResolver.getStringProp('APP_BUILD_BASE_NAME', project.getProjectDir().getParentFile().getName());
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
		return new AndroidVersion(project, version);
	}

	protected void applyAndroidPlugin() {
		project.apply plugin: AppPlugin
	}
}
