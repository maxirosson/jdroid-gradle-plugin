package com.jdroid.gradle.commons.tasks

import com.jdroid.gradle.commons.BaseGradleExtension
import com.jdroid.gradle.commons.CommandExecutor
import com.jdroid.gradle.commons.PropertyResolver
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import java.io.IOException

abstract class AbstractTask : DefaultTask() {

    protected lateinit var propertyResolver: PropertyResolver
    protected lateinit var commandExecutor: CommandExecutor
    var logLevel: LogLevel? = null
    val extension: BaseGradleExtension by lazy { project.extensions.getByName("jdroid") as BaseGradleExtension }

    @TaskAction
    fun doExecute() {
        propertyResolver = PropertyResolver(project)
        commandExecutor = CommandExecutor(project, logLevel)
        try {
            onExecute()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class)
    protected abstract fun onExecute()

    protected fun log(message: String) {
        logger.log(logLevel, message)
    }
}
