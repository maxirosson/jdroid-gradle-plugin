package com.jdroid.gradle.commons.tasks;

import com.jdroid.github.IRepositoryIdProvider;
import com.jdroid.github.RepositoryId;
import com.jdroid.github.client.GitHubClient;

public abstract class AbstractGitHubTask extends AbstractTask {

	protected GitHubClient createGitHubClient() {
		GitHubClient client = new GitHubClient();
		client.setSerializeNulls(false);
		client.setOAuth2Token(getGitHubWriteToken());
		return client;
	}

	protected IRepositoryIdProvider getIRepositoryIdProvider() {
		return RepositoryId.create(getGitHubRepositoryOwner(), getGitHubRepositoryName());
	}

	protected String getGitHubWriteToken() {
		return getExtension().getGitHubWriteToken();
	}

	protected String getGitHubReadToken() {
		return getExtension().getGitHubReadToken();
	}

	protected String getGitHubRepositoryOwner() {
		return getExtension().getGitHubRepositoryOwner();
	}

	protected String getGitHubRepositoryName() {
		return getExtension().getGitHubRepositoryName();
	}

}
