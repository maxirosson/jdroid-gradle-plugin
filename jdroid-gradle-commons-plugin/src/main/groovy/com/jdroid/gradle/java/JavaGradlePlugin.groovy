package com.jdroid.gradle.java

import com.jdroid.gradle.commons.JavaBaseGradlePlugin
import org.gradle.api.Project

public abstract class JavaGradlePlugin extends JavaBaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		applyPlugin(project)

		project.compileJava {
			sourceCompatibility getJavaSourceCompatibility()
			targetCompatibility getJavaTargetCompatibility()
			options.encoding = 'UTF-8'
		}
	}

	protected abstract void applyPlugin(Project project);

	protected Class<? extends JavaGradleExtension> getExtensionClass() {
		return JavaGradleExtension.class;
	}
}