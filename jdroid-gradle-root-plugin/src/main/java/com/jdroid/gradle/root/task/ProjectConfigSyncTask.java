package com.jdroid.gradle.root.task;

import com.jdroid.gradle.commons.tasks.AbstractTask;
import com.jdroid.java.utils.FileUtils;

import java.io.File;


public class ProjectConfigSyncTask extends AbstractTask {

	public ProjectConfigSyncTask() {
		setDescription("Synchronizes the project configuration");
	}

	@Override
	protected void onExecute() {
		File rootDir = getProject().getRootDir();

		for (ProjectConfig projectConfig : ProjectConfig.values()) {
			log("Synchronizing " + projectConfig.getTarget());
			FileUtils.copyStream(getClass().getResourceAsStream(projectConfig.getSource()),
				new File(rootDir, projectConfig.getTarget()));
		}
	}
}
