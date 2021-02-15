package com.jdroid.gradle.root.task;

import com.jdroid.github.IRepositoryIdProvider;
import com.jdroid.github.Release;
import com.jdroid.github.client.GitHubClient;
import com.jdroid.github.service.ReleaseService;
import com.jdroid.gradle.commons.CommandExecutor;
import com.jdroid.java.utils.FileUtils;

import java.io.File;
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

			String releaseNotes;
			Boolean githubChangelogGeneratorEnabled = propertyResolver.getBooleanProp("GITHUB_CHANGELOG_GENERATOR_ENABLED", false);
			if (githubChangelogGeneratorEnabled) {
				releaseNotes = getReleaseNotes();
			} else {
				releaseNotes = propertyResolver.getStringProp("GIT_HUB_RELEASE_NOTES");
			}
			createRelease(releaseService, repositoryIdProvider, tagName, releaseNotes);
			log("GitHub release created: " + tagName);
		} else {
			getLogger().warn("Skipping " + tagName + " release creation because it already exists.");
		}
	}

	private String getReleaseNotes() {
		CommandExecutor commandExecutor = new CommandExecutor(getProject(), getLogLevel());
		commandExecutor.execute("github_changelog_generator --unreleased-only --no-compare-link --no-pull-requests --no-pr-wo-labels --exclude-labels task -t " + getGitHubReadToken());

		File changeLogFile = new File(getProject().getRootDir(), "/CHANGELOG.md");

		Boolean started = false;
		Boolean exit = false;
		StringBuilder builder = new StringBuilder();
		for (String line : FileUtils.readLines(changeLogFile)) {
			if (!exit) {
				if (!started && line.startsWith("## [Unreleased")) {
					started = true;
				} else if (started) {
					if (line.contains("This Change Log was automatically generated")) {
						exit = true;
					} else {
						builder.append(line);
						builder.append('\n');
					}
				}
			}
		}

		commandExecutor.execute("git add -A");
		commandExecutor.execute("git stash");

		return builder.toString().trim();
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
