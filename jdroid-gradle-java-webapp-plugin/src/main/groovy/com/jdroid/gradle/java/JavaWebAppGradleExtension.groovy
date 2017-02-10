package com.jdroid.gradle.java

public class JavaWebAppGradleExtension extends JavaGradleExtension {

	public JavaWebAppGradleExtension(JavaWebAppGradlePlugin javaWebGradlePlugin) {
		super(javaWebGradlePlugin)
	}

	public void setBuildConfigString(def out, String key) {
		setBuildConfigString(out, key, null)
	}

	public void setBuildConfigString(def out, String key, String defaultValue) {
		String constant = "		public static final String " + key + " = ";

		String value = getStringProp(key, defaultValue)
		if (value == null) {
			constant += "null"
		} else {
			constant += '\"' + value + '\"'
		}

		constant += ";"
		out.writeLine(constant)
	}

	public void setBuildConfigBoolean(def out, String key) {
		setBuildConfigBoolean(out, key, null)
	}

	public void setBuildConfigBoolean(def out, String key, Boolean defaultValue) {
		String constant = "		public static final Boolean " + key + " = ";

		Boolean value = getBooleanProp(key, defaultValue)
		if (value == null) {
			constant += "null"
		} else {
			constant += value
		}

		constant += ";"
		out.writeLine(constant)
	}

	public void setBuildConfigInteger(def out, String key) {
		setBuildConfigInteger(out, key, null)
	}

	public void setBuildConfigInteger(def out, String key, Integer defaultValue) {
		String constant = "		public static final Integer " + key + " = ";

		Integer value = getIntegerProp(key, defaultValue)
		if (value == null) {
			constant += "null"
		} else {
			constant += value
		}

		constant += ";"
		out.writeLine(constant)
	}

}