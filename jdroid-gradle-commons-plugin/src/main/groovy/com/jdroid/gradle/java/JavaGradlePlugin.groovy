package com.jdroid.gradle.java;

import com.jdroid.gradle.commons.JavaBaseGradlePlugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.api.tasks.bundling.Jar;

public abstract class JavaGradlePlugin extends JavaBaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		applyPlugin(project);

		project.compileJava {
			sourceCompatibility getJavaSourceCompatibility()
			targetCompatibility getJavaTargetCompatibility()
			options.encoding = "UTF-8"
		}

		if (isJavaDocPublicationEnabled) {
// KTS
//			import org.gradle.jvm.tasks.Jar
//
//			val dokkaJar by tasks.creating(Jar::class) {
//				group = JavaBasePlugin.DOCUMENTATION_GROUP
//				description = "Assembles Kotlin docs with Dokka"
//				classifier = "javadoc"
//				from(tasks.dokka)
//			}

			project.task("dokkaJar", type: Jar) {
				group = JavaBasePlugin.DOCUMENTATION_GROUP
				description = "Assembles Kotlin docs with Dokka"
				archiveClassifier = "javadoc"
				from project.tasks.dokka
			}
		}

		if (isSourcesPublicationEnabled) {
			project.task("sourcesJar", type: Jar) {
				archiveClassifier = "sources"
				from project.sourceSets.main.allSource
			}
		}

		if (isKotlinEnabled) {
			applyPlugin("kotlin");
			configureKotlin();
			project.compileKotlin.kotlinOptions {
				jvmTarget = getJavaTargetCompatibility()
			}
		}
	}

	protected abstract void applyPlugin(Project project);

	protected Class<? extends JavaGradleExtension> getExtensionClass() {
		return JavaGradleExtension.class;
	}
}