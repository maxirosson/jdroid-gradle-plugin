package com.jdroid.gradle.commons.versioning;

import com.jdroid.gradle.commons.tasks.AbstractTask;

public class PrintVersionTask extends AbstractTask {

	public PrintVersionTask() {
		setDescription("Prints the current version");
	}

	@Override
	protected void onExecute() {
		System.out.println(getProject().getVersion());
	}
}
