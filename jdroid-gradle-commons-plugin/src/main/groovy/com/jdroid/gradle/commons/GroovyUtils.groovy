package com.jdroid.gradle.commons

import org.gradle.api.Project
import org.gradle.api.tasks.wrapper.Wrapper

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

	public static configureGradleWrapper(Project project) {
		project.wrapper {
			distributionType = Wrapper.DistributionType.ALL
		}
	}

	public static boolean isSnapshot(Project project) {
		return project.getVersion().isSnapshot
	}

}