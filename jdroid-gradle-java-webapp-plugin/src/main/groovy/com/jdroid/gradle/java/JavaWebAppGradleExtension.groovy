package com.jdroid.gradle.java

import org.gradle.api.Project

public class JavaWebAppGradleExtension extends JavaGradleExtension {

	public JavaWebAppGradleExtension(Project project) {
		super(project)
	}

	public void setBuildConfigString(def out, String key) {
		setBuildConfigString(out, key, null)
	}

	public void setBuildConfigString(def out, String key, String defaultValue) {
		String value = propertyResolver.getStringProp(key, defaultValue)
		String constant = null
		if (value != null) {
			constant = "		const "
		} else {
			constant ="		"
		};
		constant += "val " + key + " = "

		if (value == null) {
			constant += "null"
		} else {
			constant += '\"' + value + '\"'
		}
		out.writeLine(constant)
	}

	public void setBuildConfigBoolean(def out, String key) {
		setBuildConfigBoolean(out, key, null)
	}

	public void setBuildConfigBoolean(def out, String key, Boolean defaultValue) {
		String constant = "		val " + key + ": Boolean? = ";

		Boolean value = propertyResolver.getBooleanProp(key, defaultValue)
		if (value == null) {
			constant += "null"
		} else {
			constant += value
		}
		out.writeLine(constant)
	}

	public void setBuildConfigInteger(def out, String key) {
		setBuildConfigInteger(out, key, null)
	}

	public void setBuildConfigInteger(def out, String key, Integer defaultValue) {
		String constant = "		val " + key + ": Int? = ";

		Integer value = propertyResolver.getIntegerProp(key, defaultValue)
		if (value == null) {
			constant += "null"
		} else {
			constant += value
		}
		out.writeLine(constant)
	}

}