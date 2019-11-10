package com.jdroid.gradle.commons.tasks

import com.jdroid.github.IRepositoryIdProvider
import com.jdroid.github.RepositoryId
import com.jdroid.github.client.GitHubClient

abstract class AbstractGitHubTask : AbstractTask() {

    val iRepositoryIdProvider: IRepositoryIdProvider
        get() = RepositoryId.create(gitHubRepositoryOwner, gitHubRepositoryName)

    val gitHubWriteToken: String
        get() = extension.gitHubWriteToken

    val gitHubReadToken: String
        get() = extension.gitHubReadToken

    val gitHubRepositoryOwner: String
        get() = extension.gitHubRepositoryOwner

    val gitHubRepositoryName: String
        get() = extension.gitHubRepositoryName

    fun createGitHubClient(): GitHubClient {
        val client = GitHubClient()
        client.setSerializeNulls(false)
        client.setOAuth2Token(gitHubWriteToken)
        return client
    }
}
