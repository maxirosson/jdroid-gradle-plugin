package com.jdroid.gradle.commons.tasks

import com.jdroid.gradle.commons.Version

public class IncrementPatchVersionTask extends AbstractIncrementVersionTask {

	public IncrementPatchVersionTask() {
		description = 'Increments the patch version (X.Y.Z) -> (X.Y.Z+1)'
	}

	@Override
	protected void incrementVersion(Version version) {
		version.incrementPatch()
	}
}
