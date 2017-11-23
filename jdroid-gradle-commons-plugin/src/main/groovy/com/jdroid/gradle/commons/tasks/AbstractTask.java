package com.jdroid.gradle.commons.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.Input;

public class AbstractTask extends DefaultTask {
	
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
