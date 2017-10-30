package com.jdroid.gradle.commons

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

public abstract class JavaBaseGradlePlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.afterEvaluate {
			project.tasks.withType(Test) {
				scanForTestClasses = true

				if (!jdroid.getBooleanProp('INTEGRATION_TESTS_ENABLED', true)) {
					exclude jdroid.integrationTestsPattern
				}
			}
		}

		if (org.gradle.api.JavaVersion.current().isJava8Compatible()) {
			project.tasks.withType(org.gradle.api.tasks.javadoc.Javadoc) {
				options.addStringOption('Xdoclint:none', '-quiet')
			}
		}
	}

	protected String getJavaSourceCompatibility() {
		return JavaVersion.VERSION_1_8.toString()
	}
	protected String getJavaTargetCompatibility() {
		return JavaVersion.VERSION_1_8.toString()
	}

	protected Class<? extends JavaBaseGradleExtension> getExtensionClass() {
		return JavaBaseGradleExtension.class;
	}
}