package com.jdroid.gradle.android.versioning;

import com.jdroid.gradle.android.AndroidGradlePluginExtension;
import com.jdroid.gradle.commons.PropertyResolver;
import com.jdroid.gradle.commons.versioning.Version;

public class AndroidVersion extends Version {
	
	private Integer versionCodePrefix;
	private Integer versionCodeExtraBit;
	private Integer versionCode;

	public AndroidVersion(PropertyResolver propertyResolver, AndroidGradlePluginExtension androidGradlePluginExtension, String baseVersion) {
		super(propertyResolver, androidGradlePluginExtension, baseVersion);
		versionCodePrefix = propertyResolver.getIntegerProp("VERSION_CODE_PREFIX");
		versionCodeExtraBit = propertyResolver.getIntegerProp("VERSION_CODE_EXTRA_BIT", 0);

		if (versionCodePrefix == null) {
			versionCodePrefix = androidGradlePluginExtension.getMinimumSdkVersion();
		}
		versionCode = versionCodePrefix * 10000000 + versionCodeExtraBit * 1000000 + getVersionMajor() * 10000 + getVersionMinor() * 100 + getVersionPatch();
	}
	
	@Override
	protected Integer getDefaultMaximumVersion() {
		return 99;
	}
	
	public Integer getVersionCode() {
		return versionCode;
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
