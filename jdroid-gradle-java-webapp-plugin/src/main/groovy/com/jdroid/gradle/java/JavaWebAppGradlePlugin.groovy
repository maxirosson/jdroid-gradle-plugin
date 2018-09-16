package com.jdroid.gradle.java

import org.gradle.api.Project

public class JavaWebAppGradlePlugin extends JavaGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.ext.PACKAGING = 'war'

		applyPlugin("war");
		applyPlugin("org.gretty");

		project.sourceSets.main.java.srcDirs += "build/generated"
	}

	@Override
	protected void applyPlugin(Project project) {
		applyPlugin("java");
	}

	protected Class<? extends JavaWebAppGradleExtension> getExtensionClass() {
		return JavaWebAppGradleExtension.class;
	}
}