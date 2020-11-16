package com.jdroid.gradle.android;

import org.gradle.api.Project;

public class AndroidDynamicFeatureGradlePluginExtension extends AndroidLibraryGradlePluginExtension {

	private boolean isFakeReleaseBuildTypeEnabled;
	private boolean isReleaseBuildTypeDebuggable;

	public AndroidDynamicFeatureGradlePluginExtension(Project project) {
		super(project);

		isFakeReleaseBuildTypeEnabled = propertyResolver.getBooleanProp("FAKE_RELEASE_BUILD_TYPE_ENABLED", false);
		isReleaseBuildTypeDebuggable = propertyResolver.getBooleanProp("RELEASE_BUILD_TYPE_DEBUGGABLE", false);
	}
	
	public boolean isFakeReleaseBuildTypeEnabled() {
		return isFakeReleaseBuildTypeEnabled;
	}
	
	public void setFakeReleaseBuildTypeEnabled(boolean fakeReleaseBuildTypeEnabled) {
		isFakeReleaseBuildTypeEnabled = fakeReleaseBuildTypeEnabled;
	}
	
	public boolean isReleaseBuildTypeDebuggable() {
		return isReleaseBuildTypeDebuggable;
	}
	
	public void setReleaseBuildTypeDebuggable(boolean releaseBuildTypeDebuggable) {
		isReleaseBuildTypeDebuggable = releaseBuildTypeDebuggable;
	}
}
