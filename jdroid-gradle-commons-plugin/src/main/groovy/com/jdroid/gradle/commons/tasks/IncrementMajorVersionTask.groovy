package com.jdroid.gradle.commons.tasks

import org.gradle.api.tasks.TaskAction

public class IncrementMajorVersionTask extends AbstractIncrementVersionTask {

	public IncrementMajorVersionTask() {
		description = 'Increments the major version (X.Y.Z) -> (X+1.0.0)'
	}

	@TaskAction
	public void doExecute() {
		project.jdroid.versionMajor = changeVersion("VERSION_MAJOR", null)
		project.jdroid.versionMinor = changeVersion("VERSION_MINOR", 0)
		project.jdroid.versionPatch = changeVersion("VERSION_PATCH", 0)
		project.version = project.jdroid.generateVersionName()
		commitVersionChange()
	}
}
