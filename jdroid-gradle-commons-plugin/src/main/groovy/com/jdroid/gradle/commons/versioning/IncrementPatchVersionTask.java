package com.jdroid.gradle.commons.versioning;

public class IncrementPatchVersionTask extends AbstractIncrementVersionTask {
	
	public IncrementPatchVersionTask() {
		setDescription("Increments the patch version (X.Y.Z) -> (X.Y.Z+1)");
	}

	@Override
	protected void incrementVersion(Version version) {
		version.incrementPatch();
	}

}
