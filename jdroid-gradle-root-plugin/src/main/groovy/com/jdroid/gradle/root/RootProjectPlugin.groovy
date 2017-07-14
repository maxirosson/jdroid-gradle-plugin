package com.jdroid.gradle.root

import com.jdroid.gradle.commons.BaseGradlePlugin
import org.gradle.api.Project

public class RootProjectPlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)
	}

	protected Class<? extends RootProjectExtension> getExtensionClass() {
		return RootProjectExtension.class;
	}
}