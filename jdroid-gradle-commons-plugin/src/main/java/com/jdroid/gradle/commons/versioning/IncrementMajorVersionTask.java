package com.jdroid.gradle.commons.versioning;

public class IncrementMajorVersionTask extends AbstractIncrementVersionTask {
	
	public IncrementMajorVersionTask() {
		setDescription("Increments the major version (X.Y.Z) -> (X+1.0.0)");
	}

	@Override
	protected void incrementVersion(Version version) {
		version.incrementMajor();
	}

}
