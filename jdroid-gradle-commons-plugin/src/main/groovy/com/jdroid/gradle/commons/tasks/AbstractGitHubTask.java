package com.jdroid.gradle.commons.tasks;

import com.jdroid.github.IRepositoryIdProvider;
import com.jdroid.github.RepositoryId;
import com.jdroid.github.client.GitHubClient;

public abstract class AbstractGitHubTask extends AbstractTask {

	public GitHubClient createGitHubClient() {
		GitHubClient client = new GitHubClient();
		client.setSerializeNulls(false);
		client.setOAuth2Token(getGitHubWriteToken());
		return client;
	}

	public IRepositoryIdProvider getIRepositoryIdProvider() {
		return RepositoryId.create(getGitHubRepositoryOwner(), getGitHubRepositoryName());
	}

	public String getGitHubWriteToken() {
		return getExtension().getGitHubWriteToken();
	}

	public String getGitHubReadToken() {
		return getExtension().getGitHubReadToken();
	}

	public String getGitHubRepositoryOwner() {
		return getExtension().getGitHubRepositoryOwner();
	}

	public String getGitHubRepositoryName() {
		return getExtension().getGitHubRepositoryName();
	}

}
