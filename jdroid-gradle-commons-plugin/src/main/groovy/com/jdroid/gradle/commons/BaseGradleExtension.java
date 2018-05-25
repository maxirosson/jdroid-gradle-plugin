package com.jdroid.gradle.commons;

import com.jdroid.gradle.commons.utils.StringUtils;

import org.gradle.api.Project;
import org.gradle.api.logging.LogLevel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class BaseGradleExtension {
	
	protected final Project project;
	private LogLevel logLevel = LogLevel.INFO;
	
	public PropertyResolver propertyResolver;
	private String gitHubWriteToken;
	private String gitHubRepositoryOwner;
	private String gitHubRepositoryName;
	
	public BaseGradleExtension(Project project) {
		this.project = project;
		this.propertyResolver = new PropertyResolver(project);
		
		gitHubWriteToken = propertyResolver.getStringProp("gitHubWriteToken");
		gitHubRepositoryOwner = propertyResolver.getStringProp("gitHubRepositoryOwner");
		gitHubRepositoryName = propertyResolver.getStringProp("gitHubRepositoryName");
		
	}
	
	public String getGitSha() {
		return new CommandExecutor(project, logLevel).execute("git rev-parse --short HEAD").getStandardOutput().trim();
	}
	
	public String getGitBranch() {
		String gitBranch = propertyResolver.getStringProp("GIT_BRANCH");
		if (StringUtils.isEmpty(gitBranch)) {
			gitBranch = new CommandExecutor(project, logLevel).execute("git symbolic-ref HEAD").getStandardOutput();
		}
		
		gitBranch = gitBranch.trim();
		gitBranch = gitBranch.replace("origin/", "");
		gitBranch = gitBranch.replace("refs/heads/", "");
		gitBranch = gitBranch.replace("refs/tags/", "");
		return gitBranch;
	}
	
	public String getBuildTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		df.setTimeZone(TimeZone.getDefault());
		return df.format(new Date());
	}
	
	public LogLevel getLogLevel() {
		return logLevel;
	}
	
	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}
	
	public String getGitHubWriteToken() {
		return gitHubWriteToken;
	}
	
	public void setGitHubWriteToken(String gitHubWriteToken) {
		this.gitHubWriteToken = gitHubWriteToken;
	}
	
	public String getGitHubRepositoryOwner() {
		return gitHubRepositoryOwner;
	}
	
	public void setGitHubRepositoryOwner(String gitHubRepositoryOwner) {
		this.gitHubRepositoryOwner = gitHubRepositoryOwner;
	}
	
	public String getGitHubRepositoryName() {
		return gitHubRepositoryName;
	}
	
	public void setGitHubRepositoryName(String gitHubRepositoryName) {
		this.gitHubRepositoryName = gitHubRepositoryName;
	}
	
	
}
