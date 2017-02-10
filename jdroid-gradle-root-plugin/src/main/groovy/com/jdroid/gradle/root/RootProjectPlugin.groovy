package com.jdroid.gradle.root

import com.jdroid.gradle.commons.BaseGradlePlugin
import org.gradle.api.Project

public class RootProjectPlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.getSubprojects().each {
			it.ext.VERSION_MAJOR = project.ext.VERSION_MAJOR
			it.ext.VERSION_MINOR = project.ext.VERSION_MINOR
			it.ext.VERSION_PATCH = project.ext.VERSION_PATCH
		}

	}

	protected Class<? extends RootProjectExtension> getExtensionClass() {
		return RootProjectExtension.class;
	}
}