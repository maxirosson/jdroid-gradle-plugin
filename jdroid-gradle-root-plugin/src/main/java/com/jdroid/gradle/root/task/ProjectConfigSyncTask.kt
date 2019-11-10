package com.jdroid.gradle.root.task

import com.jdroid.gradle.commons.tasks.AbstractTask
import com.jdroid.java.utils.FileUtils
import java.io.File

class ProjectConfigSyncTask : AbstractTask() {

    init {
        description = "Synchronizes the project configuration"
    }

    override fun onExecute() {
        val rootDir = project.rootDir

        for (projectConfig in ProjectConfig.values()) {
            log("Synchronizing " + projectConfig.target)
            val target = File(rootDir, projectConfig.target)
            if (!target.exists() || projectConfig.isStrict) {
                FileUtils.copyStream(javaClass.getResourceAsStream(projectConfig.source), target)
            }
        }

        commandExecutor.execute("sh ./scripts/git/init_git_hooks.sh")
    }
}
