package com.jdroid.gradle.commons

import com.jdroid.gradle.commons.utils.StringUtils
import com.jdroid.gradle.commons.utils.TypeUtils
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

		versionMajor = getIntegerProp('VERSION_MAJOR', 1)
		versionMinor = getIntegerProp('VERSION_MINOR', 0)
		versionPatch = getIntegerProp('VERSION_PATCH', 0)
		versionClassifier = getStringProp('VERSION_CLASSIFIER', null)
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

	private def getProp(String propertyName) {
		return getProp(propertyName, null)
	}

	private def getProp(String propertyName, def defaultValue) {
		return getProp(baseGradlePlugin.project, propertyName, defaultValue)
	}

	private def getProp(Project project, String propertyName, def defaultValue) {
		if (project != null && project.ext.has(propertyName)) {
			return project.ext.get(propertyName)
		} else if (System.getenv().containsKey(propertyName)) {
			return System.getenv(propertyName)
		} else {
			return defaultValue
		}
	}

	public Boolean hasProperty(String propertyName) {
		return project.ext.has(propertyName) || System.getenv().containsKey(propertyName)
	}

	public Boolean getBooleanProp(String propertyName) {
		return getBooleanProp(propertyName, null)
	}

	public Boolean getBooleanProp(String propertyName, Boolean defaultValue) {
		Object value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else {
			return TypeUtils.getBoolean(value.toString())
		}
	}

	public String getStringProp(String propertyName) {
		return getStringProp(propertyName, null)
	}

	public String getStringProp(String propertyName, String defaultValue) {
		String value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else {
			return value;
		}
	}

	public Integer getIntegerProp(String propertyName) {
		return getIntegerProp(propertyName, null)
	}

	public Integer getIntegerProp(String propertyName, Integer defaultValue) {
		Object value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else {
			return Integer.parseInt(value.toString());
		}
	}

	public Long getLongProp(String propertyName) {
		return getLongProp(propertyName, null)
	}

	public Long getLongProp(String propertyName, Long defaultValue) {
		Object value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else {
			return Long.parseLong(value.toString());
		}
	}

	public List<String> getStringListProp(String propertyName) {
		return getStringListProp(propertyName, null)
	}

	public List<String> getStringListProp(String propertyName, List<String> defaultValue) {
		Object value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else {
			return value instanceof List ? (List)value : StringUtils.splitToListWithCommaSeparator(value.toString());
		}
	}
}