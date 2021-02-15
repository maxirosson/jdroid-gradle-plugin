package com.jdroid.gradle.commons.tasks;

import com.jdroid.gradle.commons.CommandExecutor;
import com.jdroid.java.concurrent.ExecutorUtils;

import org.gradle.process.ExecResult;

import java.util.concurrent.TimeUnit;

public class GenerateChangelogTask extends AbstractGitHubTask {

	@Override
	protected void onExecute() {

		ExecutorUtils.INSTANCE.sleep(80, TimeUnit.SECONDS);

		configureGit();

		CommandExecutor commandExecutor = new CommandExecutor(getProject(), getLogLevel());
		commandExecutor.execute("github_changelog_generator --no-unreleased --no-pull-requests --no-pr-wo-labels --exclude-labels task -u " + getGitHubRepositoryOwner() + " -p " + getGitHubRepositoryName() + " -t " + getGitHubReadToken());
		commandExecutor.execute("git add CHANGELOG.md");

		ExecResult result = commandExecutor.execute("git commit -m \"Updated CHANGELOG.md\"", getProject().getRootProject().getProjectDir(), true, true);
		if (result.getExitValue() == 0) {
			commandExecutor.execute("git diff HEAD");
			commandExecutor.execute("git push origin HEAD:production");

			log("Please verify the CHANGELOG.md [" + getExtension().getRepositoryUrl() + "/blob/production/CHANGELOG.md" + "]");
		} else {
			getLogger().warn("Skipping CHANGELOG update because it already exists.");
		}
	}
}
