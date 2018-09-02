package com.jdroid.gradle.root;

import com.jdroid.gradle.commons.BaseGradlePlugin;
import com.jdroid.gradle.root.task.ProjectConfigSyncTask;
import com.jdroid.gradle.root.task.ProjectConfigValidationTask;

import org.gradle.api.Action;
import org.gradle.api.Project;

public class RootProjectPlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project);

		ProjectConfigSyncTask projectConfigSyncTask = project.getTasks().create("syncProjectConfig", ProjectConfigSyncTask.class);
		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				projectConfigSyncTask.setLogLevel(getExtension().getLogLevel());
			}
		});

		ProjectConfigValidationTask projectConfigValidationTask = project.getTasks().create("checkProjectConfig", ProjectConfigValidationTask.class);
		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				projectConfigValidationTask.setLogLevel(getExtension().getLogLevel());
			}
		});

	}

	protected Class<? extends RootProjectExtension> getExtensionClass() {
		return RootProjectExtension.class;
	}

}
