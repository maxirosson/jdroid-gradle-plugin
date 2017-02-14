package com.jdroid.gradle.android;

public class AndroidApplicationGradlePluginExtension extends AndroidGradlePluginExtension {

	public Integer versionCodePrefix
	public Integer versionCodeExtraBit

	public AndroidApplicationGradlePluginExtension(AndroidGradlePlugin androidGradlePlugin) {
		super(androidGradlePlugin)

		versionCodePrefix = getIntegerProp('VERSION_CODE_PREFIX')
		versionCodeExtraBit = getIntegerProp('VERSION_CODE_EXTRA_BIT', 0)
	}

}