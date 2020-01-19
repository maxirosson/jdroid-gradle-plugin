package com.jdroid.gradle.commons

import org.gradle.api.Project

public class GroovyUtils {

	public static configureBuildScan(Project project, Boolean publishAlways) {
		project.buildScan {
			termsOfServiceUrl = "https://gradle.com/terms-of-service"
			termsOfServiceAgree = "yes"
			if (publishAlways) {
				publishAlways()
			}
		}
	}

	public static boolean isSnapshot(Project project) {
		return project.getVersion().isSnapshot
	}

}