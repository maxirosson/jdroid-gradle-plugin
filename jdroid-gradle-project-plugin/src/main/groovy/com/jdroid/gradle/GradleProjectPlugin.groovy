package com.jdroid.gradle


import com.jdroid.gradle.java.JavaGradlePlugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication

public class GradleProjectPlugin extends JavaGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		applyPlugin("groovy");

		project.dependencies {
			compile localGroovy()
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

		if (isPublicationConfigurationEnabled) {
			def pluginMavenPublicationsClosure = {
				pluginMaven(MavenPublication) {
					from project.components.java
					if (isSourcesPublicationEnabled) {
						artifact project.sourcesJar
					}
					if (isSourcesPublicationEnabled) {
						artifact project.sourcesJar
					}
					pom(createMavenPom())
				}
			}
			pluginMavenPublicationsClosure.setDelegate(project)

			project.publishing {
				publications(pluginMavenPublicationsClosure)
			}
		}

		if (isSigningPublicationEnabled) {
			applyPlugin('signing')
			project.signing {
				required { !project.version.isSnapshot }
				sign project.publishing.publications.pluginMaven
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

	protected Class<? extends GradleProjectExtension> getExtensionClass() {
		return GradleProjectExtension.class;
	}
}