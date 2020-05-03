package com.jdroid.gradle.commons.tasks;

import com.jdroid.github.IRepositoryIdProvider;
import com.jdroid.github.Release;
import com.jdroid.github.client.GitHubClient;
import com.jdroid.github.service.ReleaseService;

import java.io.IOException;

public class CreateGitHubReleaseTask extends AbstractGitHubTask {

	public CreateGitHubReleaseTask() {
		setDescription("Create a GitHub Release");
	}

	@Override
	protected void onExecute() throws IOException {
		GitHubClient client = createGitHubClient();

		String tagName = "v" + getProject().getVersion();

		IRepositoryIdProvider repositoryIdProvider = getIRepositoryIdProvider();
		ReleaseService releaseService = new ReleaseService(client);
		
		Release release = releaseService.getReleaseByTagName(repositoryIdProvider, tagName);
		if (release == null) {
			String releaseNotes = propertyResolver.getStringProp("GIT_HUB_RELEASE_NOTES");
			createRelease(releaseService, repositoryIdProvider, tagName, releaseNotes);
			log("GitHub release created: " + tagName);
		} else {
			getLogger().warn("Skipping " + tagName + " release creation because it already exists.");
		}
	}

	private void createRelease(ReleaseService releaseService, IRepositoryIdProvider repositoryIdProvider, String name, String body) throws IOException {

		Release release = new Release();
		release.setBody(body);
		release.setDraft(false);
		release.setName(name);
		release.setTagName(name);
		release.setPrerelease(false);
		release.setTargetCommitish(getExtension().getGitBranch());

		releaseService.createRelease(repositoryIdProvider, release);
	}
}
