package com.jdroid.gradle.android.versioning;

import com.jdroid.gradle.android.AndroidGradlePluginExtension;
import com.jdroid.gradle.commons.PropertyResolver;
import com.jdroid.gradle.commons.versioning.Version;
import com.jdroid.gradle.commons.utils.ProjectUtils;

import org.gradle.api.Project;

public class AndroidVersion extends Version {
	
	private Integer versionCodePrefix;
	private Integer versionCodeExtraBit;
	
	public AndroidVersion(Project project, String version) {
		super(project, version);

		PropertyResolver propertyResolver = new PropertyResolver(project);
		versionCodePrefix = propertyResolver.getIntegerProp("VERSION_CODE_PREFIX");
		versionCodeExtraBit = propertyResolver.getIntegerProp("VERSION_CODE_EXTRA_BIT", 0);
	}

	public Integer getVersionCode() {
		if (versionCodePrefix == null) {
			versionCodePrefix = ProjectUtils.<AndroidGradlePluginExtension>getJdroidExtension(getProject()).getMinimumSdkVersion();
		}

		return versionCodePrefix * 10000000 + versionCodeExtraBit * 1000000 + getVersionMajor() * 10000 + getVersionMinor() * 100 + getVersionPatch();
	}

	public Integer getVersionCodePrefix() {
		return versionCodePrefix;
	}

	public void setVersionCodePrefix(Integer versionCodePrefix) {
		this.versionCodePrefix = versionCodePrefix;
	}

	public Integer getVersionCodeExtraBit() {
		return versionCodeExtraBit;
	}

	public void setVersionCodeExtraBit(Integer versionCodeExtraBit) {
		this.versionCodeExtraBit = versionCodeExtraBit;
	}


}
