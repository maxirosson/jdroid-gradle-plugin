package com.jdroid.gradle.commons

import com.jdroid.java.exception.UnexpectedException
import org.gradle.api.Project

public class BaseGradleExtension {

	protected final BaseGradlePlugin baseGradlePlugin

	public Integer versionMajor
	public Integer versionMinor
	public Integer versionPatch
	public String versionClassifier
	public Boolean isSnapshot

	public BaseGradleExtension(BaseGradlePlugin baseGradlePlugin) {
		this.baseGradlePlugin = baseGradlePlugin

		versionMajor = getProp('VERSION_MAJOR', 1)
		versionMinor = getProp('VERSION_MINOR', 0)
		versionPatch = getProp('VERSION_PATCH', 0)
		versionClassifier = getProp('VERSION_CLASSIFIER', null)
		isSnapshot = getBooleanProp('SNAPSHOT', true)
		if (versionClassifier == null) {
			versionClassifier =  isSnapshot ? "SNAPSHOT" : null
		}
	}

	public String generateVersionName() {
		String versionName = "${versionMajor}.${versionMinor}.${versionPatch}"
		if (versionClassifier != null && !versionClassifier.isEmpty()) {
			versionName = versionName + "-" + versionClassifier
		}
		return versionName;
	}

	public String getGitSha() {
		return 'git rev-parse --short HEAD'.execute().text.trim()
	}

	public String getGitBranch() {
		return 'git symbolic-ref HEAD'.execute().text.trim().replaceAll(".*/", "")
	}

	public String getBuildTime() {
		def df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
		df.setTimeZone(TimeZone.getDefault())
		return df.format(new Date())
	}

	public def getProp(String propertyName) {
		return getProp(propertyName, null)
	}

	public def getProp(String propertyName, def defaultValue) {
		def value = getProp(baseGradlePlugin.project, propertyName)
		if (value == null) {
			value = System.getenv(propertyName)
		}
		return value != null ? value : defaultValue
	}

	public def getProp(Project project, String propertyName) {
		return project != null && project.hasProperty(propertyName) ? project.ext.get(propertyName) : null
	}

	public Boolean getBooleanProp(String propertyName, Boolean defaultValue) {
		def value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else if (value.toString() == 'true') {
			return true
		} else if (value.toString() == 'false') {
			return false
		} else {
			throw new UnexpectedException("Invalid Boolean value: " + value)
		}
	}

	public String getStringProp(String propertyName, String defaultValue) {
		def value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else {
			return value.toString();
		}
	}

	public Integer getIntegerProp(String propertyName, Integer defaultValue) {
		def value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else {
			return Integer.parseInt(value);
		}
	}

	public Long getLongProp(String propertyName, Long defaultValue) {
		def value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else {
			return Long.parseLong(value);
		}
	}

	public List<String> getStringListProp(String propertyName, List<String> defaultValue) {
		def value = getProp(propertyName)
		if (value == null) {
			return []
		} else {
			return (List)value;
		}
	}
}