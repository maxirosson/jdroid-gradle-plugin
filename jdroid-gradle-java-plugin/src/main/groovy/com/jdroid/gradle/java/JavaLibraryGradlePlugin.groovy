package com.jdroid.gradle.java

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication

public class JavaLibraryGradlePlugin extends JavaGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		if (isKotlinEnabled) {
			applyPlugin("kotlin");
			configureKotlin();
		}

		if (isPublicationConfigurationEnabled) {
			def javaLibraryPublicationsClosure = {
				javaLibrary(MavenPublication) {
					from project.components.java
					if (isSourcesPublicationEnabled) {
						artifact project.sourcesJar
					}
					if (isJavaDocPublicationEnabled) {
						artifact project.javadocJar
					}
					pom(createMavenPom())
				}
			}
			javaLibraryPublicationsClosure.setDelegate(project)

			project.publishing {
				publications(javaLibraryPublicationsClosure)
			}
		}

		if (isSigningPublicationEnabled) {
			applyPlugin('signing')
			project.signing {
				required { !project.version.isSnapshot }
				sign project.publishing.publications.javaLibrary
			}
		}
	}

	@Override
	protected String getPackaging() {
		return "jar";
	}

	@Override
	protected void applyPlugin(Project project) {
		applyPlugin("java-library");
	}

	protected Class<? extends JavaLibraryGradleExtension> getExtensionClass() {
		return JavaLibraryGradleExtension.class;
	}
}