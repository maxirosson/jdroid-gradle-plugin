package com.jdroid.gradle.commons

import org.gradle.api.Project

public class GroovyUtils {

	public static configureBuildScan(Project project) {
		project.buildScan {
			termsOfServiceUrl = "https://gradle.com/terms-of-service"
			termsOfServiceAgree = "yes"
			if (propertyResolver.getBooleanProp("GRADLE_BUILD_SCAN_PUBLISH_ALWAYS", false)) {
				publishAlways()
			}
		}
	}

}