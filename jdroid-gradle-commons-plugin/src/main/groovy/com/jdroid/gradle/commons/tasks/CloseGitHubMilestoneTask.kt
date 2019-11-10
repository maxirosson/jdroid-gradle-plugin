package com.jdroid.gradle.commons.tasks

import com.jdroid.github.IRepositoryIdProvider
import com.jdroid.github.Milestone
import com.jdroid.github.client.GitHubClient
import com.jdroid.github.service.MilestoneService

import java.io.IOException
import java.util.Date

open class CloseGitHubMilestoneTask : AbstractGitHubTask() {

    init {
        description = "Close the GitHub Milestone"
    }

    @Throws(IOException::class)
    override fun onExecute() {
        val client = createGitHubClient()

        closeMilestone(client, iRepositoryIdProvider, "v" + project.version)

        log("Verify that the milestone is closed on Milestones [https://github.com/$gitHubRepositoryOwner/$gitHubRepositoryName/milestones]")
    }

    @Throws(IOException::class)
    private fun closeMilestone(client: GitHubClient, repositoryIdProvider: IRepositoryIdProvider, milestoneTitle: String) {

        val milestoneService = MilestoneService(client)
        for (each in milestoneService.getMilestones(repositoryIdProvider, "open")) {
            if (each.title == milestoneTitle) {

                val newMilestone = Milestone()
                newMilestone.number = each.number
                newMilestone.title = each.title
                newMilestone.description = each.description
                newMilestone.dueOn = Date()
                newMilestone.state = "closed"
                milestoneService.editMilestone(repositoryIdProvider, newMilestone)
                break
            }
        }
    }
}
