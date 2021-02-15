package com.jdroid.gradle.commons.tasks;

import com.jdroid.gradle.commons.BaseGradleExtension;
import com.jdroid.gradle.commons.CommandExecutor;
import com.jdroid.gradle.commons.PropertyResolver;
import com.jdroid.gradle.commons.utils.ProjectUtils;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;

public abstract class AbstractTask extends DefaultTask {
	
	protected PropertyResolver propertyResolver;
	private LogLevel logLevel;

	@TaskAction
	public final void doExecute() {
		propertyResolver = new PropertyResolver(getProject());
		try {
			onExecute();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected abstract void onExecute() throws IOException;
	
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

	@Input
	protected BaseGradleExtension getExtension() {
		return ProjectUtils.getJdroidExtension(getProject());
	}

	protected void configureGit() {
		CommandExecutor commandExecutor = new CommandExecutor(getProject(), getLogLevel());

		String ciGithubUserName = propertyResolver.getStringProp("GITHUB_USER_NAME");
		if (ciGithubUserName != null) {
			commandExecutor.execute("git config user.name " + ciGithubUserName);
		}

		String ciGithubUserEmail = propertyResolver.getStringProp("GITHUB_USER_EMAIL");
		if (ciGithubUserEmail != null) {
			commandExecutor.execute("git config user.email " + ciGithubUserEmail);
		}
	}
}
