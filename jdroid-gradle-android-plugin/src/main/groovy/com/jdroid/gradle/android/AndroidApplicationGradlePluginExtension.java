package com.jdroid.gradle.android;

import org.gradle.api.Project;

public class AndroidApplicationGradlePluginExtension extends AndroidGradlePluginExtension {
	
	private boolean isFakeReleaseBuildTypeEnabled;
	
	public AndroidApplicationGradlePluginExtension(Project project) {
		super(project);

		isFakeReleaseBuildTypeEnabled = propertyResolver.getBooleanProp("FAKE_RELEASE_BUILD_TYPE_ENABLED", false);
	}
	
	public boolean isFakeReleaseBuildTypeEnabled() {
		return isFakeReleaseBuildTypeEnabled;
	}
	
	public void setFakeReleaseBuildTypeEnabled(boolean fakeReleaseBuildTypeEnabled) {
		isFakeReleaseBuildTypeEnabled = fakeReleaseBuildTypeEnabled;
	}
}
