package com.jdroid.gradle.commons.tasks;

public class PrintVersionTask extends AbstractTask {

	public PrintVersionTask() {
		setDescription("Prints the current version");
	}

	@Override
	protected void onExecute() {
		System.out.println(getProject().getVersion());
	}
}
