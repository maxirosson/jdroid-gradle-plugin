package com.jdroid.gradle.android

import com.jdroid.gradle.android.task.VerifyMissingTranslationsBetweenLocalesTask
import com.jdroid.gradle.android.task.VerifyMissingTranslationsTask
import com.jdroid.gradle.commons.JavaBaseGradlePlugin
import org.gradle.api.Project

public abstract class AndroidGradlePlugin extends JavaBaseGradlePlugin {

	protected android

	public Integer minimumSdkVersion

	public void apply(Project project) {
		super.apply(project)

		applyAndroidPlugin()

		android = project.android

		if (!jdroid.isReleaseBuildTypeEnabled()) {
			project.android.variantFilter { variant ->
				if(variant.buildType.name.equals('release')) {
					variant.setIgnore(true);
				}
			}
		}

		minimumSdkVersion = jdroid.getIntegerProp('MIN_SDK_VERSION', 16)

		android.compileSdkVersion 25
		// http://developer.android.com/tools/revisions/build-tools.html
		android.buildToolsVersion "25.0.2"


		android.defaultConfig {
			minSdkVersion minimumSdkVersion
			targetSdkVersion 25

			vectorDrawables.useSupportLibrary = jdroid.getBooleanProp('VECTOR_DRAWABLES_USE_SUPPORT_LIB', true)

			// Disabled by default, because it affects Instant Run
			if (jdroid.getBooleanProp("BUILD_TIME_CONFIG_ENABLED", false)) {
				jdroid.setBuildConfigString(android.defaultConfig, "BUILD_TIME", jdroid.getBuildTime())
			}

			// Disabled by default, because it affects Instant Run
			if (jdroid.getBooleanProp("GIT_CONFIG_ENABLED", false)) {
				jdroid.setBuildConfigString(android.defaultConfig, "GIT_SHA", jdroid.getGitSha())
				jdroid.setBuildConfigString(android.defaultConfig, "GIT_BRANCH", jdroid.getGitBranch())
			}
		}

		android.compileOptions {
			incremental jdroid.getBooleanProp('INCREMENTAL_COMPILATION_ENABLED', true)
			sourceCompatibility getJavaSourceCompatibility()
			targetCompatibility getJavaTargetCompatibility()
		}

		// https://google.github.io/android-gradle-dsl/current/com.android.build.gradle.internal.dsl.DexOptions.html
		android.dexOptions {
			maxProcessCount jdroid.getIntegerProp('MAX_PROCESS_COUNT', 1)
			preDexLibraries jdroid.getBooleanProp('PRE_DEX_LIBRARIES', true)
			dexInProcess jdroid.getBooleanProp('DEX_IN_PROCESS', false)
		}

		android.lintOptions {
			checkReleaseBuilds false
			abortOnError jdroid.getBooleanProp('ABORT_ON_LINT_ERROR', true)
			disable 'ContentDescription', 'RtlEnabled', 'RtlHardcoded', 'RtlSymmetry', 'UseCompoundDrawables', 'UnknownIdInLayout', 'RequiredSize'
		}

		android.packagingOptions {
			exclude 'META-INF/LICENSE'
			exclude 'META-INF/NOTICE'
		}

		project.task('verifyMissingTranslationsBetweenLocales', type: VerifyMissingTranslationsBetweenLocalesTask)
		project.tasks.'check'.dependsOn 'verifyMissingTranslationsBetweenLocales'

		project.task('verifyMissingTranslations', type: VerifyMissingTranslationsTask)

	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidGradlePluginExtension.class;
	}

	protected abstract void applyAndroidPlugin();
}


