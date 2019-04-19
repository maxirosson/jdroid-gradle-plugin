package com.jdroid.gradle;

import com.jdroid.gradle.java.JavaGradleExtension;

import org.gradle.api.Project;

public class GradleProjectExtension extends JavaGradleExtension {
	
	public GradleProjectExtension(Project project) {
		super(project);
	}
}
