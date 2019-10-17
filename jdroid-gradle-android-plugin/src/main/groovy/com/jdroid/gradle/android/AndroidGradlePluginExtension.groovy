package com.jdroid.gradle.android;

import com.jdroid.gradle.commons.JavaBaseGradleExtension;
import org.gradle.api.Project;

public class AndroidGradlePluginExtension extends JavaBaseGradleExtension {

	private String[] resourcesDirsPaths = ['src/main/res/'];
	private String[] notDefaultLanguages = [];
	private String missingTranslationExpression = "#TODO#";
	private Integer minimumSdkVersion;
	private boolean isReleaseBuildTypeEnabled;

	public AndroidGradlePluginExtension() {
		// TODO Remove this constructor. Only for testing
		super();
	}

	public AndroidGradlePluginExtension(Project project) {
		super(project);

		minimumSdkVersion = propertyResolver.getIntegerProp('MIN_SDK_VERSION', 21);
		isReleaseBuildTypeEnabled = propertyResolver.getBooleanProp('RELEASE_BUILD_TYPE_ENABLED', false);
	}

	public boolean isReleaseBuildTypeEnabled() {
		return isReleaseBuildTypeEnabled;
	}

	public void setBuildConfigString(def flavor, String propertyName) {
		setBuildConfigString(flavor, propertyName, null);
	}

	public void setBuildConfigString(def flavor, String propertyName, Object defaultValue) {
		setBuildConfigString(flavor, propertyName, defaultValue.toString());
	}

	public void setBuildConfigString(def flavor, String propertyName, String defaultValue) {
		String value = propertyResolver.getStringProp(propertyName, defaultValue);
		def stringValue = value == null ? "null" : '"' + value + '"'
		flavor.buildConfigField "String", propertyName, stringValue
	}

	public void setBuildConfigBoolean(def flavor, String propertyName, Boolean defaultValue) {
		String value = propertyResolver.getBooleanProp(propertyName, defaultValue).toString();
		flavor.buildConfigField "Boolean", propertyName, value;
	}

	public void setBuildConfigInteger(def flavor, String propertyName, Integer defaultValue) {
		String value = propertyResolver.getIntegerProp(propertyName, defaultValue).toString()
		flavor.buildConfigField "Integer", propertyName, value
	}

	public void setResValueString(def flavor, String propertyName, String defaultValue) {
		String value = propertyResolver.getStringProp(propertyName, defaultValue)
		def stringValue = value == null ? "" : value
		flavor.resValue "string", propertyName, stringValue
	}

	public String[] getResourcesDirsPaths() {
		return resourcesDirsPaths;
	}

	public void setResourcesDirsPaths(String[] resourcesDirsPaths) {
		this.resourcesDirsPaths = resourcesDirsPaths;
	}

	public String[] getNotDefaultLanguages() {
		return notDefaultLanguages;
	}

	public void setNotDefaultLanguages(String[] notDefaultLanguages) {
		this.notDefaultLanguages = notDefaultLanguages;
	}

	public String getMissingTranslationExpression() {
		return missingTranslationExpression;
	}

	public void setMissingTranslationExpression(String missingTranslationExpression) {
		this.missingTranslationExpression = missingTranslationExpression;
	}

	public void setMinimumSdkVersion(Integer minimumSdkVersion) {
		this.minimumSdkVersion = minimumSdkVersion
	}

	public Integer getMinimumSdkVersion() {
		return minimumSdkVersion;
	}
}