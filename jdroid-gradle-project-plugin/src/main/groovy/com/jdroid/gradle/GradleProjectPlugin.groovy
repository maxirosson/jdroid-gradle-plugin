package com.jdroid.gradle

import com.jdroid.gradle.commons.JavaBaseGradlePlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.Project

public class GradleProjectPlugin extends JavaBaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.ext.PACKAGING = 'jar'

		project.apply plugin: 'groovy'
		// https://docs.gradle.org/current/userguide/javaGradle_plugin.html
		project.apply plugin: 'java-gradle-plugin'

		project.dependencies {
			compile localGroovy()
		}

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

	protected Class<? extends GradleProjectExtension> getExtensionClass() {
		return GradleProjectExtension.class;
	}
}