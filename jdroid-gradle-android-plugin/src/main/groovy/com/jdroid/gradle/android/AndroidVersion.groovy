package com.jdroid.gradle.android

import com.jdroid.gradle.commons.PropertyResolver
import com.jdroid.gradle.commons.Version
import org.gradle.api.Project

public class AndroidVersion extends Version {

	Integer versionCodePrefix
	Integer versionCodeExtraBit

	public AndroidVersion(Project project, String version) {
		super(project, version)

		PropertyResolver propertyResolver = new PropertyResolver(project);
		versionCodePrefix = propertyResolver.getIntegerProp('VERSION_CODE_PREFIX')
		versionCodeExtraBit = propertyResolver.getIntegerProp('VERSION_CODE_EXTRA_BIT', 0)
	}

	public Integer getVersionCode() {
		if (versionCodePrefix == null) {
			versionCodePrefix = project.jdroid.minimumSdkVersion
		}
		return versionCodePrefix * 10000000 + versionCodeExtraBit * 1000000 + versionMajor * 10000 + versionMinor * 100 + versionPatch
	}
}