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
			try {
				log("Validating " + projectConfig.getTarget());
				Boolean valid = StreamUtils.isEquals(getClass().getResourceAsStream(projectConfig.getSource()),
					new FileInputStream(new File(rootDir, projectConfig.getTarget())));
				if (!valid) {
					throw new RuntimeException("The file [" + projectConfig.getTarget() + "] is not up to date");
				}
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
