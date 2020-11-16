package com.jdroid.gradle.android.task;

import com.jdroid.gradle.commons.tasks.AbstractTask;

import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.api.tasks.Input;

public class VerifyMissingTranslationsBetweenLocalesTask extends AbstractTask {

	private String[] resourcesDirsPaths;
	private String[] notDefaultLanguages;

	public VerifyMissingTranslationsBetweenLocalesTask() {
		setDescription("Verify if there are missing translations between locales");
		setGroup(JavaBasePlugin.VERIFICATION_GROUP);
	}

//	@Override
//	protected void onExecute() {
//
//		boolean error = false;
//
//		for (String resourceDirPath in resourcesDirsPaths) {
//
//			File defaultLanguageValuesDir = project.file(resourceDirPath + "values");
//			for (String language in notDefaultLanguages) {
//
//				File notDefaultLanguageValuesDir = project.file(resourceDirPath + "values-" + language)
//				String[] resTypesNames = ['strings.xml', 'plurals.xml', 'array.xml']
//				for (resTypeName in resTypesNames) {
//
//					File defaultLanguageValuesFile = new File(defaultLanguageValuesDir.getAbsolutePath() + File.separator + resTypeName);
//					File notDefaultLanguageValuesFile = new File(notDefaultLanguageValuesDir.getAbsolutePath() + File.separator + resTypeName);
//
//					if (defaultLanguageValuesFile.exists()) {
//						if (!notDefaultLanguageValuesFile.exists()) {
//							getLogger().error("Missing resources file " + notDefaultLanguageValuesFile.getAbsolutePath());
//							error = true;
//						} else {
//							def defaultLanguageKeys = []
//							defaultLanguageValuesFile.eachLine {
//								String key = getKey(it);
//								if (key != null) {
//									defaultLanguageKeys.add(key);
//								}
//							}
//
//							def notDefaultLanguageKeys = []
//							notDefaultLanguageValuesFile.eachLine {
//								String key = getKey(it);
//								if (key != null) {
//									notDefaultLanguageKeys.add(key);
//								}
//							}
//
//							def commons = defaultLanguageKeys.intersect(notDefaultLanguageKeys)
//
//							defaultLanguageKeys.removeAll(commons);
//							if (!defaultLanguageKeys.isEmpty()) {
//								getLogger().error("The following keys are missing on " + notDefaultLanguageValuesFile.getAbsolutePath())
//								getLogger().error("* " + defaultLanguageKeys);
//								getLogger().error("");
//								error = true;
//							}
//
//							notDefaultLanguageKeys.removeAll(commons);
//							if (!notDefaultLanguageKeys.isEmpty()) {
//								getLogger().error("The following keys are missing on " + defaultLanguageValuesFile.getAbsolutePath());
//								getLogger().error("* " + notDefaultLanguageKeys);
//								getLogger().error("");
//								error = true;
//							}
//
//							if (!error) {
//								getLogger().error("The following i19n files match:");
//								getLogger().error("* " + defaultLanguageValuesFile.getAbsolutePath());
//								getLogger().error("* " + notDefaultLanguageValuesFile.getAbsolutePath());
//								getLogger().error("");
//							}
//						}
//					} else {
//						getLogger().info("Ignoring the following file because it doesn't exist: " + defaultLanguageValuesFile.getAbsolutePath())
//					}
//				}
//			}
//		}
//
//		if (error) {
//			getLogger().warn("Remember that the i19n files should have the same keys on the same lines. If you don't have the translation for any language, please add the key on all the files, and 'TODO' as value");
//			throw new GradleException("The translations between locales doesn't match")
//		}
//	}

//	public static String getKey(String line) {
//		String key = null;
//		if (line.trim().matches('[^!]* name="[^"]*">.*')) {
//			Matcher matcher = Pattern.compile('name="([^"]*)">').matcher(line.trim());
//			matcher.find();
//			key = matcher.group(1);
//		}
//		return key;
//	}

	@Override
	protected void onExecute() {
		// TODO
		throw new RuntimeException("Not implemented");
	}

	@Input
	String[] getResourcesDirsPaths() {
		return resourcesDirsPaths;
	}

	void setResourcesDirsPaths(String[] resourcesDirsPaths) {
		this.resourcesDirsPaths = resourcesDirsPaths;
	}

	@Input
	String[] getNotDefaultLanguages() {
		return notDefaultLanguages;
	}

	void setNotDefaultLanguages(String[] notDefaultLanguages) {
		this.notDefaultLanguages = notDefaultLanguages;
	}
}
