package com.jdroid.gradle

import com.jdroid.gradle.commons.utils.CiUtils
import com.jdroid.gradle.commons.GroovyUtils
import com.jdroid.gradle.java.JavaGradlePlugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

public class GradleProjectPlugin extends JavaGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.afterEvaluate {
			PublishingExtension publishingExtension = project.getExtensions().getByName(PublishingExtension.NAME);
			MavenPublication mavenPublication = (MavenPublication)publishingExtension.publications.getByName("pluginMaven")
			if (isPublicationConfigurationEnabled) {
				if (isSourcesPublicationEnabled) {
					mavenPublication.artifact project.sourcesJar
				}
				if (isJavaDocPublicationEnabled) {
					mavenPublication.artifact project.dokkaJar
				}
				mavenPublication.pom(createMavenPom())
			}

			if (isSigningPublicationEnabled) {
				GroovyUtils.configureSigning(project, mavenPublication)
			}
		}

		if (isPublicationConfigurationEnabled) {
			GroovyUtils.configureGradlePlugin(project)
			boolean isGradlePluginPortalEnabled = propertyResolver.getBooleanProp("GRADLE_PLUGIN_PORTAL_ENABLED", false);
			if (isGradlePluginPortalEnabled) {
				if (CiUtils.isCi()) {
					GroovyUtils.setExt(project, "gradle.publish.key", System.getenv("GRADLE_PUBLISH_KEY"));
					GroovyUtils.setExt(project, "gradle.publish.secret", System.getenv("GRADLE_PUBLISH_SECRET"));
					applyPlugin("com.gradle.plugin-publish");
					GroovyUtils.configureGradlePortalPlugin(project, jdroid);
				}
			}
		}

	}

	@Override
	protected String getPackaging() {
		return "jar";
	}

	@Override
	protected void applyPlugin(Project project) {
		// https://docs.gradle.org/current/userguide/javaGradle_plugin.html
		applyPlugin("java-gradle-plugin");
	}
}