package com.jdroid.gradle.commons.utils

import com.jdroid.gradle.commons.BaseGradleExtension

import org.gradle.api.Project

object ProjectUtils {

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : BaseGradleExtension> getJdroidExtension(project: Project): T {
        return project.extensions.getByName("jdroid") as T
    }
}
