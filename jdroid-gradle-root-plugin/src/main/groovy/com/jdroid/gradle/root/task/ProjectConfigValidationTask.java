package com.jdroid.gradle.root.task;

import com.jdroid.gradle.commons.tasks.AbstractTask;
import com.jdroid.java.utils.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ProjectConfigValidationTask extends AbstractTask {

	public ProjectConfigValidationTask() {
		setDescription("Validates if the project configuration is up to date");
	}

	@Override
	protected void onExecute() {
		File rootDir = getProject().getRootDir();

		for (ProjectConfig projectConfig : ProjectConfig.values()) {
			if (projectConfig.isEnabled(getProject())) {
				try {
					log("Validating " + projectConfig.getTarget());
					File target = new File(rootDir, projectConfig.getTarget());
					boolean valid = target.exists();
					if (valid) {
						valid = StreamUtils.isEquals(getClass().getResourceAsStream(projectConfig.getSource()), new FileInputStream(target));
					}
					if (!valid) {
						throw new RuntimeException("The file [" + projectConfig.getTarget() + "] is not up to date");
					}
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
