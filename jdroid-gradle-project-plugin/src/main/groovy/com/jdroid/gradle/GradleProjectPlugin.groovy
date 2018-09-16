package com.jdroid.gradle

import com.jdroid.gradle.commons.JavaBaseGradlePlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.Project

public class GradleProjectPlugin extends JavaBaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.ext.PACKAGING = 'jar'

		applyPlugin("groovy");

		// https://docs.gradle.org/current/userguide/javaGradle_plugin.html
		applyPlugin("java-gradle-plugin");

		project.dependencies {
			compile localGroovy()
		}

		Boolean isOpenSourceEnabled = propertyResolver.getBooleanProp("OPEN_SOURCE_ENABLED", true)
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

		project.sourceSets {
			integrationTest {
				java.srcDir project.file('src/integrationTest/java')
				resources.srcDir project.file('src/integrationTest/resources')
				compileClasspath += project.sourceSets.main.output + project.configurations.testRuntime
				runtimeClasspath += output + compileClasspath
			}
			functionalTest {
				java.srcDir project.file('src/functionalTest/java')
				resources.srcDir project.file('src/functionalTest/resources')
				compileClasspath += project.sourceSets.main.output + project.configurations.testRuntime
				runtimeClasspath += output + compileClasspath
			}
		}

		project.task('integrationTest', type: org.gradle.api.tasks.testing.Test) {
			description = 'Runs the integration tests.'
			group = 'verification'
			testClassesDirs = project.sourceSets.integrationTest.output.classesDirs
			classpath = project.sourceSets.integrationTest.runtimeClasspath
			mustRunAfter project.tasks.'test'
		}

		project.check.dependsOn project.tasks.'integrationTest'

		project.task('functionalTest', type: org.gradle.api.tasks.testing.Test) {
			description = 'Runs the functional tests.'
			group = 'verification'
			testClassesDirs = project.sourceSets.functionalTest.output.classesDirs
			classpath = project.sourceSets.functionalTest.runtimeClasspath
			mustRunAfter project.tasks.'test'
		}

		project.check.dependsOn project.tasks.'functionalTest'
	}

	protected Class<? extends GradleProjectExtension> getExtensionClass() {
		return GradleProjectExtension.class;
	}
}