package com.jdroid.gradle.commons.tasks

import org.gradle.api.tasks.TaskAction

public class IncrementMinorVersionTask extends AbstractIncrementVersionTask {

	public IncrementMinorVersionTask() {
		description = 'Increments the minor version (X.X+1.X)'
	}

	@TaskAction
	public void doExecute() {
		project.jdroid.versionMinor = changeVersion("VERSION_MINOR", null)
		project.jdroid.versionPatch = changeVersion("VERSION_PATCH", 0)
		project.version = project.jdroid.generateVersionName()
		commitVersionChange()
	}
}
