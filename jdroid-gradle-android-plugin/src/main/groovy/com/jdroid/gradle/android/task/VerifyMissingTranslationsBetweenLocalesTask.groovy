package com.jdroid.gradle.android.task

import com.jdroid.gradle.commons.tasks.AbstractTask
import org.gradle.api.GradleException
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.util.regex.Matcher
import java.util.regex.Pattern

public class VerifyMissingTranslationsBetweenLocalesTask extends AbstractTask {

	private String[] resourcesDirsPaths;
	private String[] notDefaultLanguages;

	public VerifyMissingTranslationsBetweenLocalesTask() {
		description = 'Verify if there are missing translations between locales'
		group = JavaBasePlugin.VERIFICATION_GROUP
	}

	@TaskAction
	public void doExecute() {

		Boolean error = false;

		for (String resourceDirPath in resourcesDirsPaths) {

			File defaultLanguageValuesDir = project.file(resourceDirPath + "values")
			for (String language in notDefaultLanguages) {

				File notDefaultLanguageValuesDir = project.file(resourceDirPath + "values-" + language)
				String[] resTypesNames = ['strings.xml', 'plurals.xml', 'array.xml']
				for (resTypeName in resTypesNames) {

					File defaultLanguageValuesFile = new File(defaultLanguageValuesDir.getAbsolutePath() + File.separator + resTypeName)
					File notDefaultLanguageValuesFile = new File(notDefaultLanguageValuesDir.getAbsolutePath() + File.separator + resTypeName)

					if (defaultLanguageValuesFile.exists()) {
						if (!notDefaultLanguageValuesFile.exists()) {
							logger.error('Missing resources file ' + notDefaultLanguageValuesFile.getAbsolutePath())
							error = true
						} else {
							def defaultLanguageKeys = []
							defaultLanguageValuesFile.eachLine {
								String key = getKey(it)
								if (key != null) {
									defaultLanguageKeys.add(key)
								}
							}

							def notDefaultLanguageKeys = []
							notDefaultLanguageValuesFile.eachLine {
								String key = getKey(it)
								if (key != null) {
									notDefaultLanguageKeys.add(key)
								}
							}

							def commons = defaultLanguageKeys.intersect(notDefaultLanguageKeys)

							defaultLanguageKeys.removeAll(commons)
							if (!defaultLanguageKeys.isEmpty()) {
								logger.error("The following keys are missing on " + notDefaultLanguageValuesFile.getAbsolutePath())
								logger.error("* " + defaultLanguageKeys)
								logger.error("")
								error = true
							}

							notDefaultLanguageKeys.removeAll(commons)
							if (!notDefaultLanguageKeys.isEmpty()) {
								logger.error("The following keys are missing on " + defaultLanguageValuesFile.getAbsolutePath())
								logger.error("* " + notDefaultLanguageKeys)
								logger.error("")
								error = true
							}

							if (!error) {
								logger.error("The following i19n files match:")
								logger.error("* " + defaultLanguageValuesFile.getAbsolutePath())
								logger.error("* " + notDefaultLanguageValuesFile.getAbsolutePath())
								logger.error("")
							}
						}
					} else {
						logger.info("Ignoring the following file because it doesn't exist: " + defaultLanguageValuesFile.getAbsolutePath())
					}
				}
			}
		}

		if (error) {
			logger.warn("Remember that the i19n files should have the same keys on the same lines. If you don't have the translation for any language, please add the key on all the files, and 'TODO' as value")
			throw new GradleException("The translations between locales doesn't match")
		}
	}

	public static String getKey(String line) {
		String key = null;
		if (line.trim().matches('[^!]* name="[^"]*">.*')) {
			Matcher matcher = Pattern.compile('name="([^"]*)">').matcher(line.trim());
			matcher.find();
			key = matcher.group(1)
		}
		return key;
	}


	@Input
	String[] getResourcesDirsPaths() {
		return resourcesDirsPaths
	}

	void setResourcesDirsPaths(String[] resourcesDirsPaths) {
		this.resourcesDirsPaths = resourcesDirsPaths
	}

	@Input
	String[] getNotDefaultLanguages() {
		return notDefaultLanguages
	}

	void setNotDefaultLanguages(String[] notDefaultLanguages) {
		this.notDefaultLanguages = notDefaultLanguages
	}
}
