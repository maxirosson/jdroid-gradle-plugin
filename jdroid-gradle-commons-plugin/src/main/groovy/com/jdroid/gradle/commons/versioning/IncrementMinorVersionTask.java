package com.jdroid.gradle.commons.versioning;

public class IncrementMinorVersionTask extends AbstractIncrementVersionTask {
	
	public IncrementMinorVersionTask() {
		setDescription("Increments the minor version (X.Y.Z) -> (X.Y+1.0)");
	}

	@Override
	protected void incrementVersion(Version version) {
		version.incrementMinor();
	}

}
