package com.jdroid.gradle.commons;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.publish.maven.MavenPom;

public class BaseGradleExtension {

	protected final Project project;
	private LogLevel logLevel = LogLevel.INFO;

	public PropertyResolver propertyResolver;
	private String gitHubRepositoryOwner;
	private String gitHubRepositoryName;
	private Action<MavenPom> publishingPom;
	private String publishingSnapshotsRepoUrl;
	private String publishingReleasesRepoUrl;
	private String publishingRepoUsername;
	private String publishingRepoPassword;
	private String publishingStagingProfileId;

	public BaseGradleExtension() {
		// TODO Remove this constructor. Only for testing
		project = null;
	}

	public BaseGradleExtension(Project project) {
		this.project = project;
		this.propertyResolver = new PropertyResolver(project);

		gitHubRepositoryOwner = propertyResolver.getStringProp("GITHUB_REPOSITORY_OWNER");
		gitHubRepositoryName = propertyResolver.getStringProp("GITHUB_REPOSITORY_NAME");
		publishingSnapshotsRepoUrl = propertyResolver.getStringProp("PUBLISHING_SNAPSHOTS_REPO_URL", "https://oss.sonatype.org/content/repositories/snapshots/");
		publishingReleasesRepoUrl = propertyResolver.getStringProp("PUBLISHING_RELEASES_REPO_URL", "https://oss.sonatype.org/service/local/staging/deploy/maven2/");
		publishingRepoUsername = propertyResolver.getStringProp("PUBLISHING_REPO_USERNAME");
		publishingRepoPassword = propertyResolver.getStringProp("PUBLISHING_REPO_PASSWORD");
		publishingStagingProfileId = propertyResolver.getStringProp("PUBLISHING_STAGING_PROFILE_ID");
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
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

	public Action<MavenPom> getPublishingPom() {
		return publishingPom;
	}

	public void setPublishingPom(Action<MavenPom> publishingPom) {
		this.publishingPom = publishingPom;
	}

	public String getRepositorySshUrl() {
		return "git@github.com:" + getGitHubRepositoryOwner() + "/" + getGitHubRepositoryName() + ".git";
	}

	public String getRepositoryUrl() {
		return "https://github.com/" + getGitHubRepositoryOwner() + "/" + getGitHubRepositoryName();
	}

	public String getPublishingRepoUsername() {
		return publishingRepoUsername;
	}

	public void setPublishingRepoUsername(String publishingRepoUsername) {
		this.publishingRepoUsername = publishingRepoUsername;
	}

	public String getPublishingRepoPassword() {
		return publishingRepoPassword;
	}

	public void setPublishingRepoPassword(String publishingRepoPassword) {
		this.publishingRepoPassword = publishingRepoPassword;
	}

	public String getPublishingSnapshotsRepoUrl() {
		return publishingSnapshotsRepoUrl;
	}

	public void setPublishingSnapshotsRepoUrl(String publishingSnapshotsRepoUrl) {
		this.publishingSnapshotsRepoUrl = publishingSnapshotsRepoUrl;
	}

	public String getPublishingReleasesRepoUrl() {
		return publishingReleasesRepoUrl;
	}

	public void setPublishingReleasesRepoUrl(String publishingReleasesRepoUrl) {
		this.publishingReleasesRepoUrl = publishingReleasesRepoUrl;
	}

	public String getPublishingStagingProfileId() {
		return publishingStagingProfileId;
	}

	public void setPublishingStagingProfileId(String publishingStagingProfileId) {
		this.publishingStagingProfileId = publishingStagingProfileId;
	}
}


