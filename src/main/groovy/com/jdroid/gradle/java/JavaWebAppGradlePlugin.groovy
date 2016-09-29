package com.jdroid.gradle.java

import org.gradle.api.Project

public class JavaWebAppGradlePlugin extends JavaGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.ext.PACKAGING = 'war'

		project.apply plugin: 'jetty'
	}

	protected Class<? extends JavaWebAppGradleExtension> getExtensionClass() {
		return JavaWebAppGradleExtension.class;
	}
}