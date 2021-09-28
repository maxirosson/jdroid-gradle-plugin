package com.jdroid.gradle.android;

import org.gradle.api.Project;

public class AndroidLibraryGradlePluginExtension extends AndroidGradlePluginExtension {
	
	private String resourcePrefix;
	private boolean debugClassifierEnabled;

	public AndroidLibraryGradlePluginExtension(Project project) {
		super(project);

		resourcePrefix = propertyResolver.getStringProp("RESOURCE_PREFIX");
		debugClassifierEnabled = propertyResolver.getBooleanProp("DEBUG_CLASSIFIER_ENABLED", false);
	}

	public String getResourcePrefix() {
		return resourcePrefix;
	}

	public boolean isDebugClassifierEnabled() {
		return debugClassifierEnabled;
	}
}
