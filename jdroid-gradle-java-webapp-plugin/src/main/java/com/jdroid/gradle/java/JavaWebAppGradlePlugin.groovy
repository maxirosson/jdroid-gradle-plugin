package com.jdroid.gradle.java

import org.gradle.api.Project

public class JavaWebAppGradlePlugin extends JavaGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		applyPlugin("war");
		applyPlugin("org.gretty");

		project.sourceSets.main.java.srcDirs += "build/generated"

		// TODO See if makes sense to allow war publications
//		if (isPublicationConfigurationEnabled) {
//			def javaWarPublicationsClosure = {
//				javaWar(MavenPublication) {
//					from project.components.web
//					if (isSourcesPublicationEnabled) {
//						artifact project.sourcesJar
//					}
//					if (isJavaDocPublicationEnabled) {
//						artifact project.dokkaJar
//					}
//					pom(createMavenPom())
//
//				}
//			}
//			javaWarPublicationsClosure.setDelegate(project)
//
//			project.publishing {
//				publications(javaWarPublicationsClosure)
//			}
//		}
//
//		if (isSigningPublicationEnabled) {
//			applyPlugin('signing')
//			project.signing {
//				required { true }
//				sign project.publishing.publications.javaWar
//			}
//		}
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