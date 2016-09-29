package com.jdroid.gradle.android

import com.android.build.gradle.AppPlugin
import com.jdroid.gradle.android.task.CopyApksTask
import org.gradle.api.Project

public class AndroidApplicationGradlePlugin extends AndroidGradlePlugin {

	public void apply(Project project) {
		super.apply(project);

		project.task('copyApks', type: CopyApksTask)

		Boolean ribbonizerEnabled = jdroid.getBooleanProp("RIBBONIZER_ENABLED", true)
		if (ribbonizerEnabled) {
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

		android.defaultConfig {
			versionCode generateVersionCode()
			versionName project.version

			jackOptions {
				enabled jdroid.getBooleanProp('JACK_ENABLED', false)
			}
		}

		android.signingConfigs {

			debug {
				storeFile project.file('./debug.keystore')
			}

			if (jdroid.isReleaseBuildTypeEnabled()) {
				release {
					storeFile project.file(jdroid.getProp('STORE_FILE', './debug.keystore'))
					storePassword jdroid.getProp('STORE_PASSWORD')
					keyAlias jdroid.getProp('KEY_ALIAS')
					keyPassword jdroid.getProp('KEY_PASSWORD')
				}
			}
		}
	}

	protected Integer generateVersionCode() {
		Integer versionCodePrefix = jdroid.versionCodePrefix
		if (versionCodePrefix == null) {
			versionCodePrefix = minimumSdkVersion
		}
		return versionCodePrefix * 10000000 + jdroid.versionCodeExtraBit * 1000000 + jdroid.versionMajor * 10000 + jdroid.versionMinor * 100 + jdroid.versionPatch
	}

	protected Class<? extends AndroidApplicationGradlePluginExtension> getExtensionClass() {
		return AndroidApplicationGradlePluginExtension.class;
	}

	protected void applyAndroidPlugin() {
		project.apply plugin: AppPlugin
	}
}
