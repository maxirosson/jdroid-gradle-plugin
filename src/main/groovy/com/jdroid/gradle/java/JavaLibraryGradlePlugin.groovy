package com.jdroid.gradle.java

import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

public class JavaLibraryGradlePlugin extends JavaGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.ext.PACKAGING = 'jar'

		Boolean isOpenSourceEnabled = jdroid.getBooleanProp("OPEN_SOURCE_ENABLED", true)
		if (isOpenSourceEnabled) {
			project.task('javadocJar', type: Jar) {
				classifier = 'javadoc'
				from project.javadoc
			}

			project.task('sourcesJar', type: Jar) {
				classifier = 'sources'
				from project.sourceSets.main.allSource
			}

			project.artifacts {
				archives project.tasks.javadocJar, project.tasks.sourcesJar
			}
		}
	}

	protected Class<? extends JavaLibraryGradleExtension> getExtensionClass() {
		return JavaLibraryGradleExtension.class;
	}
}