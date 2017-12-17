package com.jdroid.gradle.commons.tasks;

import com.jdroid.gradle.commons.PropertyResolver;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public abstract class AbstractTask extends DefaultTask {
	
	protected PropertyResolver propertyResolver;
	
	@TaskAction
	public final void doExecute() {
		propertyResolver = new PropertyResolver(getProject());
		onExecute();
	}
	
	protected abstract void onExecute();
	
	protected void log(String message) {
		getLogger().log(logLevel, message);
	}

	@Input
	public LogLevel getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	private LogLevel logLevel;
}
