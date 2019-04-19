package com.jdroid.gradle.java

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication

public class JavaWebAppGradlePlugin extends JavaGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		applyPlugin("war");
		applyPlugin("org.gretty");

		project.sourceSets.main.java.srcDirs += "build/generated"

		if (isPublicationConfigurationEnabled) {
			def javaWarPublicationsClosure = {
				javaWar(MavenPublication) {
					from project.components.web
					if (isSourcesPublicationEnabled) {
						archiveClassifier project.sourcesJar
					}
					if (isJavaDocPublicationEnabled) {
						archiveClassifier project.javadocJar
					}
					pom(createMavenPom())

				}
			}
			javaWarPublicationsClosure.setDelegate(project)

			project.publishing {
				publications(javaWarPublicationsClosure)
			}
		}

		if (isSigningPublicationEnabled) {
			applyPlugin('signing')
			project.signing {
				required { !project.version.isSnapshot }
				sign project.publishing.publications.javaWar
			}
		}
	}

	@Override
	protected String getPackaging() {
		return "war";
	}

	@Override
	protected void applyPlugin(Project project) {
		applyPlugin("java");
	}

	protected Class<? extends JavaWebAppGradleExtension> getExtensionClass() {
		return JavaWebAppGradleExtension.class;
	}
}