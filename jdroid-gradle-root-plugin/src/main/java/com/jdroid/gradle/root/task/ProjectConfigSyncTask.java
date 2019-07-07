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
			File target = new File(rootDir, projectConfig.getTarget());
			if (!target.exists() || projectConfig.isStrict()) {
				FileUtils.copyStream(getClass().getResourceAsStream(projectConfig.getSource()), target);
			}
		}
	}
}
