package com.jdroid.gradle.java

import com.jdroid.gradle.commons.JavaBaseGradlePlugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

public abstract class JavaGradlePlugin extends JavaBaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		applyPlugin(project)

		project.compileJava {
			sourceCompatibility getJavaSourceCompatibility()
			targetCompatibility getJavaTargetCompatibility()
			options.encoding = 'UTF-8'
		}

		if (isJavaDocPublicationEnabled) {
			project.task('javadocJar', type: Jar) {
				archiveClassifier = 'javadoc'
				from project.javadoc
			}
		}

		if (isSourcesPublicationEnabled) {
			project.task('sourcesJar', type: Jar) {
				archiveClassifier = 'sources'
				from project.sourceSets.main.allSource
			}
		}
	}

	@Override
	protected void applyKotlinPlugins() {
		applyPlugin("kotlin");
	}

	protected abstract void applyPlugin(Project project);

	protected Class<? extends JavaGradleExtension> getExtensionClass() {
		return JavaGradleExtension.class;
	}
}