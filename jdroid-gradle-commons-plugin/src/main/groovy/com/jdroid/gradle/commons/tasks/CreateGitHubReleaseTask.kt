package com.jdroid.gradle.commons.tasks

import com.jdroid.github.IRepositoryIdProvider
import com.jdroid.github.Release
import com.jdroid.github.client.RequestException
import com.jdroid.github.service.ReleaseService
import java.io.IOException

open class CreateGitHubReleaseTask : AbstractGitHubTask() {
    init {
        description = "Create a GitHub Release"
    }

    @Throws(IOException::class)
    override fun onExecute() {
        val client = createGitHubClient()

        val tagName = "v" + project.version

        val repositoryIdProvider = iRepositoryIdProvider
        val releaseService = ReleaseService(client)

        try {
            releaseService.getReleaseByTagName(repositoryIdProvider, tagName)
            logger.warn("Skipping $tagName release creation because it already exists.")
        } catch (e: RequestException) {
            if (e.status == 404) {
                val releaseNotes = propertyResolver.getStringProp("GIT_HUB_RELEASE_NOTES")
                createRelease(releaseService, repositoryIdProvider, tagName, releaseNotes)
                log("GitHub release created: $tagName")
            } else {
                throw e
            }
        }
    }

    @Throws(IOException::class)
    private fun createRelease(releaseService: ReleaseService, repositoryIdProvider: IRepositoryIdProvider, name: String, body: String) {

        val release = Release()
        release.body = body
        release.isDraft = false
        release.name = name
        release.tagName = name
        release.isPrerelease = false
        release.targetCommitish = extension.gitBranch

        releaseService.createRelease(repositoryIdProvider, release)
    }
}
