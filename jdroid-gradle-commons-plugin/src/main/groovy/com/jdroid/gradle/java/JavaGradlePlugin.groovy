package com.jdroid.gradle.java

import com.jdroid.gradle.commons.JavaBaseGradlePlugin
import org.gradle.api.Project

public class JavaGradlePlugin extends JavaBaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.apply plugin: 'java'

		project.compileJava {
			sourceCompatibility getJavaSourceCompatibility()
			targetCompatibility getJavaTargetCompatibility()
		}
	}

	protected Class<? extends JavaGradleExtension> getExtensionClass() {
		return JavaGradleExtension.class;
	}
}