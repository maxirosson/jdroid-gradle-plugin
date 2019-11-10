package com.jdroid.gradle.root.task

import com.jdroid.gradle.commons.tasks.AbstractTask
import com.jdroid.java.utils.StreamUtils

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class ProjectConfigValidationTask : AbstractTask() {
    init {
        description = "Validates if the project configuration is up to date"
    }

    override fun onExecute() {
        val rootDir = project.rootDir

        for (projectConfig in ProjectConfig.values()) {
            try {
                log("Validating " + projectConfig.target)
                val target = File(rootDir, projectConfig.target)
                var valid = target.exists()
                if (valid && projectConfig.isStrict) {
                    valid = StreamUtils.isEquals(javaClass.getResourceAsStream(projectConfig.source), FileInputStream(target))
                }
                if (!valid) {
                    throw RuntimeException("The file [" + projectConfig.target + "] is not up to date")
                }
            } catch (e: FileNotFoundException) {
                throw RuntimeException(e)
            }
        }
    }
}
