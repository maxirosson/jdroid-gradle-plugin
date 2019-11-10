package com.jdroid.gradle.commons.tasks

import com.jdroid.java.concurrent.ExecutorUtils
import java.util.concurrent.TimeUnit

open class GenerateChangelogTask : AbstractGitHubTask() {

    override fun onExecute() {

        ExecutorUtils.sleep(80, TimeUnit.SECONDS)

        commandExecutor.execute("github_changelog_generator --no-unreleased --no-pull-requests --no-pr-wo-labels --exclude-labels task -t $gitHubReadToken")
        commandExecutor.execute("git add CHANGELOG.md")

        val result = commandExecutor.execute("git commit -m \"Updated CHANGELOG.md\"", project.rootProject.projectDir, true, true)
        if (result.exitValue == 0) {
            commandExecutor.execute("git diff HEAD")
            commandExecutor.execute("git push origin HEAD:production")

            log("Please verify the CHANGELOG.md [" + extension.repositoryUrl + "/blob/production/CHANGELOG.md" + "]")
        } else {
            logger.warn("Skipping CHANGELOG update because it already exists.")
        }
    }
}
