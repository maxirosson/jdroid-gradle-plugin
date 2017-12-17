package com.jdroid.gradle.commons

import com.jdroid.gradle.commons.utils.StringUtils
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

public class BaseGradleExtension {

	protected final Project project
	private LogLevel logLevel = LogLevel.INFO;
	public PropertyResolver propertyResolver;

	public BaseGradleExtension(Project project) {
		this.project = project
		this.propertyResolver = new PropertyResolver(project);
	}

	public String getGitSha() {
		return 'git rev-parse --short HEAD'.execute().text.trim()
	}

	public String getGitBranch() {
		String gitBranch = propertyResolver.getStringProp('GIT_BRANCH');
		if (StringUtils.isEmpty(gitBranch)) {
			gitBranch = 'git symbolic-ref HEAD'.execute().text
		}
		gitBranch = gitBranch.trim().replace("origin/", "").replace("refs/heads/", "")
		return gitBranch
	}

	public String getBuildTime() {
		def df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
		df.setTimeZone(TimeZone.getDefault())
		return df.format(new Date())
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}
}