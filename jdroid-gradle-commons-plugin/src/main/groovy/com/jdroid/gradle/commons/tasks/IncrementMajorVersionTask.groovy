package com.jdroid.gradle.commons.tasks

import com.jdroid.gradle.commons.Version

public class IncrementMajorVersionTask extends AbstractIncrementVersionTask {

	public IncrementMajorVersionTask() {
		description = 'Increments the major version (X+1.X.X)'
	}

	@Override
	protected void incrementVersion(Version version) {
		version.incrementMajor()
	}
}
