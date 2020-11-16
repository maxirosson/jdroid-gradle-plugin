package com.jdroid.gradle.java;

import com.jdroid.gradle.commons.JavaBaseGradleExtension;

import org.gradle.api.Project;


public class JavaGradleExtension extends JavaBaseGradleExtension {
	
	public JavaGradleExtension(Project project) {
		super(project);
	}

	public String getBuildConfigStringLine(String key) {
		return getBuildConfigStringLine(key, null);
	}

	public String getBuildConfigStringLine(String key, String defaultValue) {
		String value = propertyResolver.getStringProp(key, defaultValue);
		String constant = null;
		if (value != null) {
			constant = "		const ";
		} else {
			constant = "		";
		};
		constant += "val " + key + " = ";

		if (value == null) {
			constant += "null";
		} else {
			constant += '\"' + value + '\"';
		}
		return constant;
	}

	public String getBuildConfigBooleanLine(String key) {
		return getBuildConfigBooleanLine(key, null);
	}

	public String getBuildConfigBooleanLine(String key, Boolean defaultValue) {
		String constant = "		val " + key + ": Boolean? = ";

		Boolean value = propertyResolver.getBooleanProp(key, defaultValue);
		if (value == null) {
			constant += "null";
		} else {
			constant += value;
		}
		return constant;
	}

	public String getBuildConfigIntegerLine(String key) {
		return getBuildConfigIntegerLine(key, null);
	}

	public String getBuildConfigIntegerLine(String key, Integer defaultValue) {
		String constant = "		val " + key + ": Int? = ";

		Integer value = propertyResolver.getIntegerProp(key, defaultValue);
		if (value == null) {
			constant += "null";
		} else {
			constant += value;
		}
		return constant;
	}
}
