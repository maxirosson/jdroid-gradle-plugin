package com.jdroid.gradle.commons.tasks;

import org.apache.tools.ant.types.Commandline;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.Input;
import org.gradle.process.ExecResult;
import org.gradle.process.ExecSpec;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class AbstractTask extends DefaultTask {
	
	public ExtendedExecResult execute(String command, File workingDirectory, Boolean logStandardOutput, Boolean ignoreExitValue) {
		log("Executing command: " + command);
		
		ByteArrayOutputStream standardOutputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream errorOutputStream = new ByteArrayOutputStream();
		
		ExecResult execResult = getProject().exec(new Action<ExecSpec>() {
			@Override
			public void execute(ExecSpec execSpec) {
				if (workingDirectory != null) {
					execSpec.setWorkingDir(workingDirectory);
				}
				execSpec.setCommandLine((Object[])Commandline.translateCommandline(command));
				execSpec.setIgnoreExitValue(ignoreExitValue);
				if (logStandardOutput) {
					execSpec.setStandardOutput(standardOutputStream);
				}
				execSpec.setErrorOutput(errorOutputStream);
			}
		});
		if (standardOutputStream.size() > 0) {
			log(standardOutputStream.toString());
		}
		if (errorOutputStream.size() > 0) {
			getLogger().error(errorOutputStream.toString());
		}
		return new ExtendedExecResult(execResult, standardOutputStream, errorOutputStream);
	}

	public ExecResult execute(String command, File workingDirectory) {
		return execute(command, workingDirectory, true, false);
	}

	public ExecResult execute(String command) {
		return execute(command, getProject().getRootProject().getProjectDir());
	}

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
