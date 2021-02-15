package com.jdroid.gradle.root.task;

import com.jdroid.github.IRepositoryIdProvider;
import com.jdroid.github.Milestone;
import com.jdroid.github.client.GitHubClient;
import com.jdroid.github.service.MilestoneService;

import java.io.IOException;
import java.util.Date;

public class CloseGitHubMilestoneTask extends AbstractGitHubTask {
	
	public CloseGitHubMilestoneTask() {
		setDescription("Close the GitHub Milestone");
	}

	@Override
	protected void onExecute() throws IOException {
		GitHubClient client = createGitHubClient();

		closeMilestone(client, getIRepositoryIdProvider(), "v" + getProject().getVersion());

		log("Verify that the milestone is closed on Milestones [https://github.com/" + getGitHubRepositoryOwner() + "/" + getGitHubRepositoryName() + "/milestones]");
	}

	private void closeMilestone(GitHubClient client, IRepositoryIdProvider repositoryIdProvider, String milestoneTitle) throws IOException {

		MilestoneService milestoneService = new MilestoneService(client);
		for (Milestone each : milestoneService.getMilestones(repositoryIdProvider, "open")) {
			if (each.getTitle().equals(milestoneTitle)) {

				Milestone newMilestone = new Milestone();
				newMilestone.setNumber(each.getNumber());
				newMilestone.setTitle(each.getTitle());
				newMilestone.setDescription(each.getDescription());
				newMilestone.setDueOn(new Date());
				newMilestone.setState("closed");
				milestoneService.editMilestone(repositoryIdProvider, newMilestone);
				break;
			}

		}

	}

}
