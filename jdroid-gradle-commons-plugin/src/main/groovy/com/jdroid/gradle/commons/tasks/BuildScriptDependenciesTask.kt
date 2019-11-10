package com.jdroid.gradle.commons.tasks

open class BuildScriptDependenciesTask : AbstractTask() {

    override fun onExecute() {
        val classpathConfiguration = project.buildscript.configurations.findByName("classpath")
        if (classpathConfiguration != null) {
            for (each in classpathConfiguration.asPath.split(":")) {
                println(each)
            }
        }
    }
}
