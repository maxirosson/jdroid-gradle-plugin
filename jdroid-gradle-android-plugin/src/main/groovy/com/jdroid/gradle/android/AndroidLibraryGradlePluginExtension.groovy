package com.jdroid.gradle.android;

public class AndroidLibraryGradlePluginExtension extends AndroidGradlePluginExtension {

	private String resourcePrefix
	private Boolean publishNonDefault

	public AndroidLibraryGradlePluginExtension(AndroidGradlePlugin androidGradlePlugin) {
		super(androidGradlePlugin)

		resourcePrefix = getProp('RESOURCE_PREFIX')
		publishNonDefault = getBooleanProp('PUBLISH_NON_DEFAULT', true)
	}

	public String getResourcePrefix() {
		return resourcePrefix
	}

	public Boolean getPublishNonDefault() {
		return publishNonDefault
	}
}
