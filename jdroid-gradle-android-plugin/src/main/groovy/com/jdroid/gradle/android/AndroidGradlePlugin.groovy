package com.jdroid.gradle.android

import com.jdroid.gradle.android.task.VerifyMissingTranslationsBetweenLocalesTask
import com.jdroid.gradle.android.task.VerifyMissingTranslationsTask
import com.jdroid.gradle.commons.JavaBaseGradlePlugin
import org.gradle.api.Project

public abstract class AndroidGradlePlugin extends JavaBaseGradlePlugin {

	private static final int ANDROID_SDK_VERSION = 26

	// http://developer.android.com/tools/revisions/build-tools.html
	private static final String ANDROID_BUILD_TOOLS_VERSION = '26.0.1'

	protected android

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

		android.compileSdkVersion jdroid.getIntegerProp('ANDROID_COMPILE_SDK_VERSION', ANDROID_SDK_VERSION)
		android.buildToolsVersion jdroid.getStringProp('ANDROID_BUILD_TOOLS_VERSION', ANDROID_BUILD_TOOLS_VERSION)

		android.defaultConfig {
			minSdkVersion jdroid.minimumSdkVersion
			targetSdkVersion jdroid.getIntegerProp('ANDROID_BUILD_TOOLS_VERSION', ANDROID_SDK_VERSION)

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
			if (jdroid.hasProp('DEX_IN_PROCESS')) {
				dexInProcess jdroid.getBooleanProp('DEX_IN_PROCESS')
			}
			// Only used if dexInProcess = false
			if (jdroid.hasProp('JAVA_MAX_HEAP_SIZE')) {
				javaMaxHeapSize jdroid.getStringProp('JAVA_MAX_HEAP_SIZE')
			}

		}

		android.lintOptions {
			checkReleaseBuilds false
			abortOnError jdroid.getBooleanProp('ABORT_ON_LINT_ERROR', true)
			disable 'ContentDescription', 'RtlEnabled', 'RtlHardcoded', 'RtlSymmetry', 'UseCompoundDrawables', 'UnknownIdInLayout', 'RequiredSize'
		}

		android.packagingOptions {
			exclude 'META-INF/LICENSE'
			exclude 'META-INF/NOTICE'
			exclude 'META-INF/LICENSE-FIREBASE_jvm.txt'
			exclude 'META-INF/LICENSE-FIREBASE_android.txt'
			exclude 'NOTICE_FIREBASE_jvm'
			exclude 'NOTICE_FIREBASE_android'
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


