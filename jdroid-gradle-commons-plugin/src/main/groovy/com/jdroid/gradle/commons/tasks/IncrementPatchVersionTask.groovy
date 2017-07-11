package com.jdroid.gradle.commons.tasks

import org.gradle.api.tasks.TaskAction

public class IncrementPatchVersionTask extends AbstractIncrementVersionTask {

	public IncrementPatchVersionTask() {
		description = 'Increments the patch version (X.Y.Z) -> (X.Y.Z+1)'
	}

	@TaskAction
	public void doExecute() {
		project.jdroid.versionPatch = changeVersion("VERSION_PATCH", null)
		project.version = project.jdroid.generateVersionName()
		commitVersionChange()
	}
}
