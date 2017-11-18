package com.jdroid.gradle.android;

import org.gradle.api.Project;

public class AndroidLibraryGradlePluginExtension extends AndroidGradlePluginExtension {
	
	private String resourcePrefix;
	private Boolean publishNonDefault;
	
	public AndroidLibraryGradlePluginExtension(Project project) {
		super(project);

		resourcePrefix = getStringProp("RESOURCE_PREFIX");
		publishNonDefault = getBooleanProp("PUBLISH_NON_DEFAULT", true);
	}

	public String getResourcePrefix() {
		return resourcePrefix;
	}

	public Boolean getPublishNonDefault() {
		return publishNonDefault;
	}
}
