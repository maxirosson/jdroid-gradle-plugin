package com.jdroid.gradle.android;

import org.gradle.api.Project;

public class AndroidLibraryGradlePluginExtension extends AndroidGradlePluginExtension {
	
	private String resourcePrefix;

	public AndroidLibraryGradlePluginExtension(Project project) {
		super(project);

		resourcePrefix = propertyResolver.getStringProp("RESOURCE_PREFIX");
	}

	public String getResourcePrefix() {
		return resourcePrefix;
	}
}
