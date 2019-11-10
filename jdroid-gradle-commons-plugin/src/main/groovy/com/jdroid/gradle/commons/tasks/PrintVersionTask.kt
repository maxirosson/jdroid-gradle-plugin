package com.jdroid.gradle.commons.tasks

open class PrintVersionTask : AbstractTask() {

    init {
        description = "Prints the current version"
    }

    override fun onExecute() {
        println(project.version)
    }
}
