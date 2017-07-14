package com.jdroid.gradle.android

import com.jdroid.gradle.commons.JavaBaseGradleExtension

public class AndroidGradlePluginExtension extends JavaBaseGradleExtension {

	String[] resourcesDirsPaths = ['src/main/res/']
	String[] notDefaultLanguages = []
	String missingTranslationExpression = "#TODO#"
	Integer minimumSdkVersion

	private boolean isReleaseBuildTypeEnabled

	public AndroidGradlePluginExtension(AndroidGradlePlugin androidGradlePlugin) {
		super(androidGradlePlugin)

		minimumSdkVersion = getIntegerProp('MIN_SDK_VERSION', 16)
		isReleaseBuildTypeEnabled = getBooleanProp('RELEASE_BUILD_TYPE_ENABLED', false)
	}

	public boolean isReleaseBuildTypeEnabled() {
		return isReleaseBuildTypeEnabled
	}

	public void setBuildConfigString(def flavor, String propertyName) {
		setBuildConfigString(flavor, propertyName, null)
	}

	public void setBuildConfigString(def flavor, String propertyName, Object defaultValue) {
		setBuildConfigString(flavor, propertyName, defaultValue.toString())
	}

	public void setBuildConfigString(def flavor, String propertyName, String defaultValue) {
		String value = getStringProp(propertyName, defaultValue)
		def stringValue = value == null ? "null" : '"' + value + '"'
		flavor.buildConfigField "String", propertyName, stringValue
	}

	public void setBuildConfigBoolean(def flavor, String propertyName, Boolean defaultValue) {
		String value = getBooleanProp(propertyName, defaultValue).toString()
		flavor.buildConfigField "Boolean", propertyName, value
	}

	public void setBuildConfigInteger(def flavor, String propertyName, Integer defaultValue) {
		String value = getIntegerProp(propertyName, defaultValue).toString()
		flavor.buildConfigField "Integer", propertyName, value
	}

	public void setResValueString(def flavor, String propertyName, String defaultValue) {
		String value = getStringProp(propertyName, defaultValue)
		def stringValue = value == null ? "" : value
		flavor.resValue "string", propertyName, stringValue
	}
}