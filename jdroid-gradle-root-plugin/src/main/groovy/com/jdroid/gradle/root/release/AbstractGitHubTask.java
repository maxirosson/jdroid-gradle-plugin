package com.jdroid.gradle.root.release;

import com.jdroid.github.IRepositoryIdProvider;
import com.jdroid.github.RepositoryId;
import com.jdroid.github.client.GitHubClient;
import com.jdroid.gradle.commons.tasks.AbstractTask;

import org.gradle.api.tasks.Input;

public abstract class AbstractGitHubTask extends AbstractTask {

	protected GitHubClient createGitHubClient() {
		GitHubClient client = new GitHubClient();
		client.setSerializeNulls(false);
		client.setOAuth2Token(getGitHubWriteToken());
		return client;
	}

	@Input
	protected IRepositoryIdProvider getIRepositoryIdProvider() {
		return RepositoryId.create(getGitHubRepositoryOwner(), getGitHubRepositoryName());
	}

	@Input
	protected String getGitHubWriteToken() {
		return getExtension().getGitHubWriteToken();
	}

	@Input
	protected String getGitHubReadToken() {
		return getExtension().getGitHubReadToken();
	}

	@Input
	protected String getGitHubRepositoryOwner() {
		return getExtension().getGitHubRepositoryOwner();
	}

	@Input
	protected String getGitHubRepositoryName() {
		return getExtension().getGitHubRepositoryName();
	}

}
