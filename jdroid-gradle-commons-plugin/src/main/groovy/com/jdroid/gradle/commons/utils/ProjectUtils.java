package com.jdroid.gradle.commons.utils;

import com.jdroid.gradle.commons.BaseGradleExtension;

import org.gradle.api.Project;

public class ProjectUtils {
	
	public static <T extends BaseGradleExtension> T getJdroidExtension(Project project) {
		return ((T)project.getExtensions().getByName("jdroid"));
	}
}
