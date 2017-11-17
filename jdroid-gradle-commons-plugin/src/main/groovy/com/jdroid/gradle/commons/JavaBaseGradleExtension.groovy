package com.jdroid.gradle.commons

import org.gradle.api.Project

public class JavaBaseGradleExtension extends BaseGradleExtension {

	private String integrationTestsPattern = "**/integration/**/*Test.class"

	public JavaBaseGradleExtension(Project project) {
		super(project)
	}

	String getIntegrationTestsPattern() {
		return integrationTestsPattern
	}

	void setIntegrationTestsPattern(String integrationTestsPattern) {
		this.integrationTestsPattern = integrationTestsPattern
	}
}