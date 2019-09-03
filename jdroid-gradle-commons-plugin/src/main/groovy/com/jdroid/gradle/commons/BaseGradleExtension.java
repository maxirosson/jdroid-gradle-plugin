package com.jdroid.gradle.commons;

import com.jdroid.gradle.commons.utils.StringUtils;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.publish.maven.MavenPom;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class BaseGradleExtension {
	
	protected final Project project;
	private LogLevel logLevel = LogLevel.INFO;
	
	public PropertyResolver propertyResolver;
	private String gitHubWriteToken;
	private String gitHubReadToken;
	private String gitHubRepositoryOwner;
	private String gitHubRepositoryName;
	private String gitHubUserName;
	private String gitHubUserEmail;
	private Action<MavenPom> mavenPom;
	private String snapshotsMavenRepo;
	private String releasesMavenRepo;
	private String nexusUsername;
	private String nexusPassword;

	public BaseGradleExtension(Project project) {
		this.project = project;
		this.propertyResolver = new PropertyResolver(project);
		
		gitHubWriteToken = propertyResolver.getStringProp("GITHUB_WRITE_TOKEN");
		gitHubReadToken = propertyResolver.getStringProp("GITHUB_READ_TOKEN");
		gitHubRepositoryOwner = propertyResolver.getStringProp("GITHUB_REPOSITORY_OWNER");
		gitHubRepositoryName = propertyResolver.getStringProp("GITHUB_REPOSITORY_NAME");
		gitHubUserName = propertyResolver.getStringProp("GITHUB_USER_NAME");
		gitHubUserEmail = propertyResolver.getStringProp("GITHUB_USER_EMAIL");
		snapshotsMavenRepo = propertyResolver.getStringProp("https://oss.sonatype.org/content/repositories/snapshots/");
		releasesMavenRepo = propertyResolver.getStringProp("https://oss.sonatype.org/service/local/staging/deploy/maven2/");
		nexusUsername = propertyResolver.getStringProp("NEXUS_USERNAME");
		nexusPassword = propertyResolver.getStringProp("NEXUS_PASSWORD");
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

	public String getGitHubUserName() {
		return gitHubUserName;
	}

	public String getGitHubUserEmail() {
		return gitHubUserEmail;
	}

	public Action<MavenPom> getMavenPom() {
		return mavenPom;
	}

	public void setMavenPom(Action<MavenPom> mavenPom) {
		this.mavenPom = mavenPom;
	}

	public String getGitHubReadToken() {
		return gitHubReadToken;
	}

	public void setGitHubReadToken(String gitHubReadToken) {
		this.gitHubReadToken = gitHubReadToken;
	}

	public String getRepositorySshUrl() {
		return "git@github.com:" + getGitHubRepositoryOwner() + "/" + getGitHubRepositoryName() + ".git";
	}

	public String getRepositoryUrl() {
		return "https://github.com/" + getGitHubRepositoryOwner() + "/" + getGitHubRepositoryName();
	}

	public String getNexusUsername() {
		return nexusUsername;
	}

	public void setNexusUsername(String nexusUsername) {
		this.nexusUsername = nexusUsername;
	}

	public String getNexusPassword() {
		return nexusPassword;
	}

	public void setNexusPassword(String nexusPassword) {
		this.nexusPassword = nexusPassword;
	}

	public String getSnapshotsMavenRepo() {
		return snapshotsMavenRepo;
	}

	public void setSnapshotsMavenRepo(String snapshotsMavenRepo) {
		this.snapshotsMavenRepo = snapshotsMavenRepo;
	}

	public String getReleasesMavenRepo() {
		return releasesMavenRepo;
	}

	public void setReleasesMavenRepo(String releasesMavenRepo) {
		this.releasesMavenRepo = releasesMavenRepo;
	}
}
