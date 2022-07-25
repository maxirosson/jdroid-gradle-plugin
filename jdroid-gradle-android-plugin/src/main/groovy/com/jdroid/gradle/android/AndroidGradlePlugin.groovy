package com.jdroid.gradle.android;

import com.android.build.gradle.BaseExtension;
import com.jdroid.gradle.android.task.VerifyMissingTranslationsBetweenLocalesTask;
import com.jdroid.gradle.android.task.VerifyMissingTranslationsTask;
import com.jdroid.gradle.commons.JavaBaseGradlePlugin;
import org.gradle.api.Action;
import org.gradle.api.Project;

public abstract class AndroidGradlePlugin extends JavaBaseGradlePlugin {

	private static final int ANDROID_SDK_VERSION = 30;

	// http://developer.android.com/tools/revisions/build-tools.html
	private static final String ANDROID_BUILD_TOOLS_VERSION = "30.0.3";

	// TODO These versions should be defined on Libs/BuildLibs.kt
	// https://github.com/maxirosson/jdroid-android-lint/releases
	private static final String JDROID_ANDROID_LINT_RULES_VERSION = "1.4.0";

	protected BaseExtension android;

	public void apply(Project project) {
		super.apply(project)

		project.repositories.google();

		applyAndroidPlugin();

		android = project.android;

		if (propertyResolver.getBooleanProp("JDROID_ANDROID_LINT_RULES_ENABLED", false)) {
			addDependency("lintChecks", "com.jdroidtools","jdroid-android-lint-rules", propertyResolver.getStringProp("JDROID_ANDROID_LINT_RULES_VERSION", JDROID_ANDROID_LINT_RULES_VERSION));
		}

		if (!jdroid.isReleaseBuildTypeEnabled()) {
			project.android.variantFilter { variant ->
				if(variant.buildType.name.equals("release")) {
					variant.setIgnore(true);
				}
			}
		}

		android.setCompileSdkVersion(propertyResolver.getIntegerProp("ANDROID_COMPILE_SDK_VERSION", ANDROID_SDK_VERSION));
		android.setBuildToolsVersion(propertyResolver.getStringProp("ANDROID_BUILD_TOOLS_VERSION", ANDROID_BUILD_TOOLS_VERSION));
		android.getDefaultConfig().setMinSdkVersion(jdroid.minimumSdkVersion);
		android.getDefaultConfig().setTargetSdkVersion(propertyResolver.getIntegerProp("ANDROID_TARGET_SDK_VERSION", ANDROID_SDK_VERSION));

		android.defaultConfig {

			vectorDrawables.useSupportLibrary = propertyResolver.getBooleanProp("VECTOR_DRAWABLES_USE_SUPPORT_LIB", true)
		}

		android.compileOptions {
			incremental propertyResolver.getBooleanProp("INCREMENTAL_COMPILATION_ENABLED", true)
			sourceCompatibility getJavaSourceCompatibility()
			targetCompatibility getJavaTargetCompatibility()
		}

		android.kotlinOptions {
			jvmTarget = getJavaTargetCompatibility()
		}

		// https://google.github.io/android-gradle-dsl/current/com.android.build.gradle.internal.dsl.DexOptions.html
		//android.getDexOptions().setMaxProcessCount(propertyResolver.getIntegerProp("MAX_PROCESS_COUNT", 1));
//		if (propertyResolver.hasProp("DEX_IN_PROCESS")) {
//			android.getDexOptions().setDexInProcess(propertyResolver.getBooleanProp("DEX_IN_PROCESS"));
//		}
		// Only used if dexInProcess = false
//		if (propertyResolver.hasProp("JAVA_MAX_HEAP_SIZE")) {
//			android.getDexOptions().setJavaMaxHeapSize(propertyResolver.getStringProp("JAVA_MAX_HEAP_SIZE"));
//		}

		// http://tools.android.com/tips/lint-checks
		android.lintOptions {
			checkReleaseBuilds false
			abortOnError propertyResolver.getBooleanProp("ABORT_ON_LINT_ERROR", true)
			enable "ConvertToWebp"
			disable "CheckResult",
				"ClickableViewAccessibility",
				"ContentDescription",
				"CustomViewStyleable",
				"LabelFor",
				"Overdraw",
				"RequiredSize",
				"UnknownIdInLayout",
				"UnusedAttribute",
				"UnusedResources",
				"UseCompoundDrawables",
				"UseSparseArrays",
				"VectorPath"
			warning "Instantiatable" // TODO This error stated to happen on AGP 4.0. See if we can stop ignoring it on a future version
			error "AlwaysShowAction",
				"Autofill",
				"ButtonStyle",
				"ConstantLocale",
				"DefaultLocale",
				"Deprecated",
				"DisableBaselineAlignment",
				"DrawAllocation",
				"ExifInterface",
				"ExportedReceiver",
				"GradleDynamicVersion",
				"HardcodedText",
				"IconDipSize",
				"IconLocation",
				"IgnoreWithoutReason",
				"InefficientWeight",
				"InlinedApi",
				"IntentReset",
				"NestedWeights",
				"ObsoleteLayoutParam",
				"ObsoleteSdkInt",
				"PrivateApi",
				"PrivateResource",
				"Recycle",
				"RedundantNamespace",
				"RelativeOverlap",
				"RtlEnabled",
				"ScrollViewSize",
				"SetTextI18n",
				"ShiftFlags",
				"SimpleDateFormat",
				"SpUsage",
				"StaticFieldLeak",
				"StringFormatCount",
				"StringFormatMatches",
				"SuspiciousImport",
				"SwitchIntDef",
				"TextFields",
				"UnusedNamespace",
				"UselessLeaf",
				"UselessParent"
		}

		android.packagingOptions {
			exclude "META-INF/LICENSE"
			exclude "META-INF/NOTICE"
			exclude "META-INF/LICENSE-FIREBASE_jvm.txt"
			exclude "META-INF/LICENSE-FIREBASE_android.txt"
			exclude "NOTICE_FIREBASE_jvm"
			exclude "NOTICE_FIREBASE_android"
		}

		android.testOptions {
			unitTests {
				// Flag to support unit tests that require Android resources, such as Robolectric.
				// When you set this property to true, the plugin performs resource, asset, and manifest merging before running your unit tests.
				includeAndroidResources = true
			}
		}

		VerifyMissingTranslationsBetweenLocalesTask verifyMissingTranslationsBetweenLocalesTask =
				project.task("verifyMissingTranslationsBetweenLocales", type: VerifyMissingTranslationsBetweenLocalesTask)
		project.tasks."check".dependsOn "verifyMissingTranslationsBetweenLocales"

		VerifyMissingTranslationsTask verifyMissingTranslationsTask = project.task("verifyMissingTranslations", type: VerifyMissingTranslationsTask)

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


