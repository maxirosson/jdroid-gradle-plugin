package com.jdroid.gradle.android.task
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.TaskAction

public class VerifyMissingTranslationsTask extends DefaultTask {

	public VerifyMissingTranslationsTask() {
		description = 'Verify if there are missing translations on any string resource.'
		group = JavaBasePlugin.VERIFICATION_GROUP
	}

	@TaskAction
	public void doExecute() {

		Boolean error = false;

		for (String resourceDirPath in project.jdroid.resourcesDirsPaths) {
			File resDirFile = project.file(resourceDirPath)
			for (file in resDirFile.listFiles()) {
				if (file.isDirectory() && file.getName().startsWith("values")) {
					String[] resTypesNames = ['strings.xml', 'plurals.xml', 'array.xml']
					for (resTypesName in resTypesNames) {
						String resourceFilePath = file.getAbsolutePath() + File.separator + resTypesName
						File resourceFile = new File(resourceFilePath)
						if (resourceFile.exists()) {
							logger.info('Verified translations [' + project.jdroid.missingTranslationExpression + '] on ' + resourceFilePath)
							if (resourceFile.text.contains(project.jdroid.missingTranslationExpression)) {
								logger.error('Missing translations [' + project.jdroid.missingTranslationExpression + '] on ' + resourceFilePath)
								error = true
							}
						}
					}
				}
			}
		}

		if (error) {
			throw new GradleException('Missing translations [' + project.jdroid.missingTranslationExpression + ']')
		}
	}
}
