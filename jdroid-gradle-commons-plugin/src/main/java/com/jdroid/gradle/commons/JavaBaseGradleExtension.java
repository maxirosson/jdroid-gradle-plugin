package com.jdroid.gradle.commons;

import org.gradle.api.Project;

public class JavaBaseGradleExtension extends BaseGradleExtension {
	
	private String integrationTestsPattern = "**/integration/**/*Test.class";

	public JavaBaseGradleExtension() {
		// TODO Remove this constructor. Only for testing
		super();
	}

	public JavaBaseGradleExtension(Project project) {
		super(project);
	}
	
	public String getIntegrationTestsPattern() {
		return integrationTestsPattern;
	}

	public void setIntegrationTestsPattern(String integrationTestsPattern) {
		this.integrationTestsPattern = integrationTestsPattern;
	}
}
