package com.jdroid.gradle.commons.tasks

import com.jdroid.gradle.commons.Version

public class IncrementMinorVersionTask extends AbstractIncrementVersionTask {

	public IncrementMinorVersionTask() {
		description = 'Increments the minor version (X.Y.Z) -> (X.Y+1.0)'
	}

	@Override
	protected void incrementVersion(Version version) {
		version.incrementMinor()
	}
}
