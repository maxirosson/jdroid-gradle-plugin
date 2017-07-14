package com.jdroid.gradle.commons.tasks

import com.jdroid.gradle.commons.Version

public class IncrementMinorVersionTask extends AbstractIncrementVersionTask {

	public IncrementMinorVersionTask() {
		description = 'Increments the minor version (X.X+1.X)'
	}

	@Override
	protected void incrementVersion(Version version) {
		version.incrementMinor()
	}
}
