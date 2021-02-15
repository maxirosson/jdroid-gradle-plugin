package com.jdroid.gradle.commons

import org.gradle.api.Project
import org.gradle.api.tasks.wrapper.Wrapper

public class GroovyUtils {

	public static configureBuildScan(Project project, Boolean publishAlways) {
		project.buildScan {
			termsOfServiceUrl = "https://gradle.com/terms-of-service"
			termsOfServiceAgree = "yes"
			if (CiUtils.isCi()) {
				if (publishAlways) {
					publishAlways()
				} else {
					publishOnFailure()
				}
			}
		}
	}

	public static configureGradleWrapper(Project project) {
		project.wrapper {
			distributionType = Wrapper.DistributionType.ALL
		}
	}

	public static configureSigning(Project project, Object publications) {
		Map<String, String> map = new HashMap<>();
		map.put("plugin", "signing");
		project.apply(map);
		project.signing {
			required { true }
			sign publications
		}
		boolean isCI = System.getenv("CI") == "true"
		if (isCI) {
			project.ext["signing.keyId"] = System.getenv('SIGNING_KEY_ID')
			project.ext["signing.password"] = System.getenv('SIGNING_PASSWORD')
			project.ext["signing.secretKeyRingFile"] = System.getenv('SIGNING_SECRET_KEY_RING_FILE')
		}
	}

	public static setExt(Project project, String key, Object value) {
		project.ext[key] = value
	}

	public static configureGradlePortalPlugin(Project project, BaseGradleExtension jdroid) {
		project.gradlePlugin {
			plugins {
				plugin {
					id = project.ext.GRADLE_PLUGIN_PORTAL_ID
					implementationClass = project.ext.GRADLE_PLUGIN_PORTAL_IMPLEMENTATION_CLASS
				}
			}
		}

		project.pluginBundle {
			website = jdroid.repositoryUrl
			vcsUrl = jdroid.repositoryUrl
			description = project.ext.description
			tags = project.ext.GRADLE_PLUGIN_PORTAL_TAGS

			plugins {
				byeByeJetifierPlugin {
					displayName = project.ext.PROJECT_NAME
				}
			}

			mavenCoordinates {
				groupId = project.group
				artifactId = project.ext.ARTIFACT_ID
			}
		}
	}
}