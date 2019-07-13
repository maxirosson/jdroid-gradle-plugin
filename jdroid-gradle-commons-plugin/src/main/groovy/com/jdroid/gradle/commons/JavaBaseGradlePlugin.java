package com.jdroid.gradle.commons;

import com.jdroid.java.collections.Lists;

import org.gradle.api.Action;
import org.gradle.api.JavaVersion;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.javadoc.Javadoc;
import org.gradle.api.tasks.testing.Test;
import org.gradle.external.javadoc.CoreJavadocOptions;

public abstract class JavaBaseGradlePlugin extends BaseGradlePlugin {

	private static final String KOTLIN_VERSION = "1.3.40";

	protected Boolean isJavaDocPublicationEnabled;
	public Boolean isKotlinEnabled;
	public Boolean isKtLintEnabled;

	public void apply(Project project) {
		super.apply(project);

		project.afterEvaluate(new Action<Project>() {
			@Override
			public void execute(Project it) {
				for (Test each : it.getTasks().withType(Test.class)) {
					each.setScanForTestClasses(true);
					if (!propertyResolver.getBooleanProp("INTEGRATION_TESTS_ENABLED", true)) {
						each.exclude(((JavaBaseGradleExtension)jdroid).getIntegrationTestsPattern());
					}
				}
			}
		});

		if (JavaVersion.current().isJava8Compatible()) {
			project.afterEvaluate(new Action<Project>() {
				@Override
				public void execute(Project it) {
					for (Javadoc each : it.getTasks().withType(org.gradle.api.tasks.javadoc.Javadoc.class)) {
						((CoreJavadocOptions)each.getOptions()).addStringOption("Xdoclint:none", "-quiet");
					};
				}
			});
		}

		isKotlinEnabled = propertyResolver.getBooleanProp("KOTLIN_ENABLED", true);
		isKtLintEnabled = propertyResolver.getBooleanProp("KTLINT_ENABLED", isKotlinEnabled);

		isJavaDocPublicationEnabled = propertyResolver.getBooleanProp("JAVADOC_PUBLICATION_ENABLED", false);
		if (isJavaDocPublicationEnabled) {
			applyDokkaPlugin();
		}
	}

	protected void applyDokkaPlugin() {
		applyPlugin("org.jetbrains.dokka");
	}

	protected void configureKotlin() {
		addDependency("implementation", "org.jetbrains.kotlin", "kotlin-stdlib-jdk8", KOTLIN_VERSION);
		addDependency("implementation", "org.jetbrains.kotlin", "kotlin-reflect", KOTLIN_VERSION);
		// TODO See how to configure this
//			project.compileKotlin {
//				kotlinOptions {
//					jvmTarget = getJavaTargetCompatibility();
//				}
//			}
//			project.compileTestKotlin {
//				kotlinOptions {
//					jvmTarget = getJavaTargetCompatibility();
//				}
//			}
		if (isKtLintEnabled) {
			configureKtlint();
		}
	}

	protected void configureKtlint() {
		addConfiguration("ktlint");
		addDependency("ktlint", "com.pinterest", "ktlint", "0.33.0");
		Task ktlintTask = project.getTasks().create("ktlint", JavaExec.class, new Action<JavaExec>() {
			@Override
			public void execute(JavaExec javaExec) {
				javaExec.setDescription("Check Kotlin code style.");
				javaExec.setMain("com.pinterest.ktlint.Main");
				javaExec.setClasspath(project.getConfigurations().findByName("ktlint"));

				// to generate report in checkstyle format prepend following args:
				// "--reporter=plain", "--reporter=checkstyle,output=${buildDir}/ktlint.xml"
				javaExec.setArgs(Lists.newArrayList("src/**/*.kt"));
			}
		});
		ktlintTask.setGroup("verification");
		project.getTasks().findByName("check").dependsOn(ktlintTask);

		Task ktlintFormatTask = project.getTasks().create("ktlintFormat", JavaExec.class, new Action<JavaExec>() {
			@Override
			public void execute(JavaExec javaExec) {
				javaExec.setDescription("Fix Kotlin code style deviations.");
				javaExec.setMain("com.pinterest.ktlint.Main");
				javaExec.setClasspath(project.getConfigurations().findByName("ktlint"));
				javaExec.setArgs(Lists.newArrayList("-F", "src/**/*.kt"));
			}
		});
		ktlintFormatTask.setGroup("formatting");
	}

	protected String getJavaSourceCompatibility() {
		return JavaVersion.VERSION_1_8.toString();
	}

	protected String getJavaTargetCompatibility() {
		return JavaVersion.VERSION_1_8.toString();
	}

	protected Class<? extends JavaBaseGradleExtension> getExtensionClass() {
		return JavaBaseGradleExtension.class;
	}

}
