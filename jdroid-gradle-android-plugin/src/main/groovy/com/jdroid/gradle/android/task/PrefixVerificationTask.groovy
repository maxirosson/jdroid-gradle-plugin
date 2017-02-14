package com.jdroid.gradle.android.task
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.TaskAction

import java.util.regex.Matcher
import java.util.regex.Pattern

public class PrefixVerificationTask extends DefaultTask {

	public PrefixVerificationTask() {
		group = JavaBasePlugin.VERIFICATION_GROUP
	}

	@TaskAction
	public void doExecute() {
		String prefix = project.jdroid.getStringProp('RESOURCE_PREFIX')

		if (prefix != null) {

			Set<File> dirsToVerify = []
			project.android.sourceSets.each {
				dirsToVerify.addAll(it.res.srcDirs)
			}

			// Verify the res files prefix

			List<String> dirPrefixesToVerify = ["drawable", "layout", "menu", "xml", "raw", "anim"]
			List<String> filePrefixErrors = []

			dirsToVerify.each { File res ->
				res.listFiles().each {
					dirPrefixesToVerify.each { String dirPrefixToVerify ->
						if (it.isDirectory() && it.name.startsWith(dirPrefixToVerify)) {
							verifyFilePrefix(it, prefix, filePrefixErrors)
						}
					}
				}
			}

			if (!filePrefixErrors.isEmpty()) {
				println "The following files should start with the prefix: " + prefix
				filePrefixErrors.each {
					println " - " + it
				}
			}

			// Verify the values res names

			List<String> valuesResNamesPrefixErrors = []

			dirsToVerify.each { File res ->

				res.listFiles().each {
					if (it.isDirectory() && it.name.startsWith("values")) {
						verifyValuesResNamePrefix(it, prefix, valuesResNamesPrefixErrors);
					}
				}
			}

			if (!valuesResNamesPrefixErrors.isEmpty()) {
				println "The following values res names should start with the prefix: " + prefix
				valuesResNamesPrefixErrors.each {
					println " - " + it
				}
			}

			if (!filePrefixErrors.isEmpty() || !valuesResNamesPrefixErrors.isEmpty()) {
				throw new GradleException("Prefix verification failed")
			}
		}
	}

	protected void verifyFilePrefix(File dirFile, String prefix, List<String> errors) {
		List<String> extensionsToVerify = [".xml", ".png"]
		dirFile.listFiles().each { File file ->
			String extension = file.name.replace(file.name.replaceFirst(~/\.[^\.]+$/, ''), "")
			if (!file.isDirectory() && extensionsToVerify.contains(extension) && !file.getName().startsWith(prefix)) {
				errors.add(file.absolutePath)
			}
		}
	}

	protected void verifyValuesResNamePrefix(File dirFile, String prefix, List<String> errors) {
		List<String> filesToIgnore = project.jdroid.getStringListProp('RESOURCE_FILES_TO_SKIP_PREFIX_VALIDATION', [])
		dirFile.listFiles().each { File file ->
			if (!filesToIgnore.contains(file.getName())) {
				file.eachLine { String line ->
					String name = getName(line)
					if (name != null && !name.toLowerCase().startsWith(prefix)) {
						errors.add(file.absolutePath + " -> " + name)
					}
				}
			}
		}
	}

	public static String getName(String line) {

		// TODO Verify the attr elements that are outside of a declare-styleable

		List<String> elementsToVerify = ["string", "plurals", "color", "dimen", "declare-styleable", "style", "bool"]
		String key = null;
		for(String each : elementsToVerify) {
			if (line.trim().matches('^<\\s*' + each + '\\s.*name="[^"]*".*>$')) {
				Matcher matcher = Pattern.compile('name="([^"]*)".*>$').matcher(line.trim());
				matcher.find();
				key = matcher.group(1)
				break;
			}
		}
		return key;
	}

}
