package com.jdroid.gradle.commons;

import org.gradle.api.Action;
import org.gradle.api.JavaVersion;
import org.gradle.api.Project;
import org.gradle.api.tasks.javadoc.Javadoc;
import org.gradle.api.tasks.testing.Test;
import org.gradle.external.javadoc.CoreJavadocOptions;

public abstract class JavaBaseGradlePlugin extends BaseGradlePlugin {

	protected Boolean isJavaDocPublicationEnabled;

	public void apply(Project project) {
		super.apply(project);

		isJavaDocPublicationEnabled = propertyResolver.getBooleanProp("JAVADOC_PUBLICATION_ENABLED", false);

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
