package com.jdroid.gradle.android

import com.jdroid.gradle.android.task.VerifyMissingTranslationsBetweenLocalesTask
import com.jdroid.gradle.android.task.VerifyMissingTranslationsTask
import com.jdroid.gradle.commons.JavaBaseGradlePlugin
import org.gradle.api.Action
import org.gradle.api.Project

public abstract class AndroidGradlePlugin extends JavaBaseGradlePlugin {

	private static final int ANDROID_SDK_VERSION = 27

	// http://developer.android.com/tools/revisions/build-tools.html
	private static final String ANDROID_BUILD_TOOLS_VERSION = '27.0.2'

	protected android

	public void apply(Project project) {
		super.apply(project)

		project.repositories.google()

		applyAndroidPlugin()

		android = project.android

		if (!jdroid.isReleaseBuildTypeEnabled()) {
			project.android.variantFilter { variant ->
				if(variant.buildType.name.equals('release')) {
					variant.setIgnore(true);
				}
			}
		}

		android.compileSdkVersion propertyResolver.getIntegerProp('ANDROID_COMPILE_SDK_VERSION', ANDROID_SDK_VERSION)
		android.buildToolsVersion propertyResolver.getStringProp('ANDROID_BUILD_TOOLS_VERSION', ANDROID_BUILD_TOOLS_VERSION)

		android.defaultConfig {
			minSdkVersion jdroid.minimumSdkVersion
			targetSdkVersion propertyResolver.getIntegerProp('ANDROID_TARGET_SDK_VERSION', ANDROID_SDK_VERSION)

			vectorDrawables.useSupportLibrary = propertyResolver.getBooleanProp('VECTOR_DRAWABLES_USE_SUPPORT_LIB', true)

			// Disabled by default, because it affects Instant Run
			if (propertyResolver.getBooleanProp("BUILD_TIME_CONFIG_ENABLED", false)) {
				jdroid.setBuildConfigString(android.defaultConfig, "BUILD_TIME", jdroid.getBuildTime())
			}

			// Disabled by default, because it affects Instant Run
			if (propertyResolver.getBooleanProp("GIT_CONFIG_ENABLED", false)) {
				jdroid.setBuildConfigString(android.defaultConfig, "GIT_SHA", jdroid.getGitSha())
				jdroid.setBuildConfigString(android.defaultConfig, "GIT_BRANCH", jdroid.getGitBranch())
			}
		}

		android.compileOptions {
			incremental propertyResolver.getBooleanProp('INCREMENTAL_COMPILATION_ENABLED', true)
			sourceCompatibility getJavaSourceCompatibility()
			targetCompatibility getJavaTargetCompatibility()
		}

		// https://google.github.io/android-gradle-dsl/current/com.android.build.gradle.internal.dsl.DexOptions.html
		android.dexOptions {
			maxProcessCount propertyResolver.getIntegerProp('MAX_PROCESS_COUNT', 1)
			preDexLibraries propertyResolver.getBooleanProp('PRE_DEX_LIBRARIES', true)
			if (propertyResolver.hasProp('DEX_IN_PROCESS')) {
				dexInProcess propertyResolver.getBooleanProp('DEX_IN_PROCESS')
			}
			// Only used if dexInProcess = false
			if (propertyResolver.hasProp('JAVA_MAX_HEAP_SIZE')) {
				javaMaxHeapSize propertyResolver.getStringProp('JAVA_MAX_HEAP_SIZE')
			}

		}

		android.lintOptions {
			checkReleaseBuilds false
			abortOnError propertyResolver.getBooleanProp('ABORT_ON_LINT_ERROR', true)
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

		android.testOptions {
			unitTests {
				// Flag to support unit tests that require Android resources, such as Robolectric.
				// When you set this property to true, the plugin performs resource, asset, and manifest merging before running your unit tests.
				includeAndroidResources = true
			}
		}

		VerifyMissingTranslationsBetweenLocalesTask verifyMissingTranslationsBetweenLocalesTask =
				project.task('verifyMissingTranslationsBetweenLocales', type: VerifyMissingTranslationsBetweenLocalesTask)
		project.tasks.'check'.dependsOn 'verifyMissingTranslationsBetweenLocales'

		VerifyMissingTranslationsTask verifyMissingTranslationsTask = project.task('verifyMissingTranslations', type: VerifyMissingTranslationsTask)

		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				verifyMissingTranslationsBetweenLocalesTask.setLogLevel(project.jdroid.getLogLevel());
				verifyMissingTranslationsBetweenLocalesTask.setResourcesDirsPaths(project.jdroid.getResourcesDirsPaths());
				verifyMissingTranslationsBetweenLocalesTask.setNotDefaultLanguages(project.jdroid.getNotDefaultLanguages());

				verifyMissingTranslationsTask.setLogLevel(project.jdroid.getLogLevel());
				verifyMissingTranslationsTask.setResourcesDirsPaths(project.jdroid.getResourcesDirsPaths());
				verifyMissingTranslationsTask.setMissingTranslationExpression(project.jdroid.getMissingTranslationExpression());
			}
		});

	}

	protected Class<? extends AndroidGradlePluginExtension> getExtensionClass() {
		return AndroidGradlePluginExtension.class;
	}

	protected abstract void applyAndroidPlugin();
}


