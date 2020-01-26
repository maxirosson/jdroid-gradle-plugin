package com.jdroid.gradle.root.task;

import com.jdroid.gradle.commons.CommandExecutor;
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
			if (projectConfig.isEnabled(getProject())) {
				log("Synchronizing " + projectConfig.getTarget());
				File target = new File(rootDir, projectConfig.getTarget());
				FileUtils.copyStream(getClass().getResourceAsStream(projectConfig.getSource()), target);
			}
		}

		CommandExecutor commandExecutor = new CommandExecutor(getProject(), getLogLevel());
		commandExecutor.execute("sh ./scripts/git/init_git_hooks.sh");
	}
}
