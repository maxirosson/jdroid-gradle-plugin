package com.jdroid.gradle.android.task

import com.jdroid.gradle.android.AndroidGradlePluginExtension
import com.jdroid.gradle.commons.tasks.AbstractTask
import com.jdroid.gradle.commons.utils.ProjectUtils
import org.gradle.api.GradleException
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.Input

public class VerifyMissingTranslationsTask extends AbstractTask {

	private String[] resourcesDirsPaths
	private String missingTranslationExpression

	public VerifyMissingTranslationsTask() {
		description = 'Verify if there are missing translations on any string resource.'
		group = JavaBasePlugin.VERIFICATION_GROUP
	}

	@Override
	protected void onExecute() {

		Boolean error = false;

		for (String resourceDirPath in resourcesDirsPaths) {
			File resDirFile = project.file(resourceDirPath)
			for (file in resDirFile.listFiles()) {
				if (file.isDirectory() && file.getName().startsWith("values")) {
					String[] resTypesNames = ['strings.xml', 'plurals.xml', 'array.xml']
					for (resTypesName in resTypesNames) {
						String resourceFilePath = file.getAbsolutePath() + File.separator + resTypesName
						File resourceFile = new File(resourceFilePath)
						if (resourceFile.exists()) {
							log('Verified translations [' + missingTranslationExpression + '] on ' + resourceFilePath)
							if (resourceFile.text.contains(missingTranslationExpression)) {
								logger.error('Missing translations [' + missingTranslationExpression + '] on ' + resourceFilePath)
								error = true
							}
						}
					}
				}
			}
		}

		if (error) {
			throw new GradleException('Missing translations [' + ProjectUtils.<AndroidGradlePluginExtension>getJdroidExtension(project).getMissingTranslationExpression() + ']')
		}
	}

	@Input
	String[] getResourcesDirsPaths() {
		return resourcesDirsPaths
	}

	void setResourcesDirsPaths(String[] resourcesDirsPaths) {
		this.resourcesDirsPaths = resourcesDirsPaths
	}

	@Input
	String getMissingTranslationExpression() {
		return missingTranslationExpression
	}

	void setMissingTranslationExpression(String missingTranslationExpression) {
		this.missingTranslationExpression = missingTranslationExpression
	}
}
