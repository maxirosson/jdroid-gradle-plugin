package com.jdroid.gradle.android.task

import com.jdroid.gradle.android.AndroidGradlePluginExtension
import com.jdroid.gradle.commons.tasks.AbstractTask
import com.jdroid.gradle.commons.utils.ProjectUtils
import com.jdroid.java.collections.Lists.newArrayList
import org.gradle.api.GradleException
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.Input
import java.io.File

class VerifyMissingTranslationsTask : AbstractTask() {

    @get:Input
    lateinit var resourcesDirsPaths: List<String>

    @get:Input
    var missingTranslationExpression: String? = null


    init {
        description = "Verify if there are missing translations on any string resource."
        group = JavaBasePlugin.VERIFICATION_GROUP
    }

    override fun onExecute() {
        var error = false
        for (resourceDirPath in resourcesDirsPaths) {
            val resDirFile = project.file(resourceDirPath)
            for (file in resDirFile.listFiles()) {
                if (file.isDirectory && file.name.startsWith("values")) {
                    for (resTypesName in listOf("strings.xml", "plurals.xml", "array.xml")) {
                        val resourceFilePath = file.absolutePath + File.separator + resTypesName
                        val resourceFile = File(resourceFilePath)
                        if (resourceFile.exists()) {
                            log("Verified translations [$missingTranslationExpression] on $resourceFilePath")
                            if (resourceFile.text.contains(missingTranslationExpression)) {
                                logger.error("Missing translations [$missingTranslationExpression] on $resourceFilePath")
                                error = true
                            }
                        }
                    }
                }
            }
        }
        if (error) {
            throw GradleException(
                "Missing translations [" + ProjectUtils.getJdroidExtension<AndroidGradlePluginExtension>(
                    project
                ).missingTranslationExpression + "]"
            )
        }
    }
}