package com.jdroid.gradle.android.task;

import com.jdroid.gradle.commons.tasks.AbstractTask
import com.jdroid.gradle.commons.utils.ListUtils
import org.gradle.api.GradleException;
import org.gradle.api.plugins.JavaBasePlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrefixVerificationTask extends AbstractTask {

	public PrefixVerificationTask() {
		group = JavaBasePlugin.VERIFICATION_GROUP;
	}

	@Override
	protected void onExecute() {
		String prefix = propertyResolver.getStringProp('RESOURCE_PREFIX');

		if (prefix != null) {

			Set<File> dirsToVerify = new HashSet<>();
			project.android.sourceSets.each {
				dirsToVerify.addAll(it.res.srcDirs)
			}

			// Verify the res files prefix

			List<String> dirPrefixesToVerify = ListUtils.newArrayList("drawable", "layout", "menu", "xml", "raw", "anim");
			List<String> filePrefixErrors = ListUtils.newArrayList();

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
				logger.error("The following files should start with the prefix: " + prefix);
				for (String each : filePrefixErrors) {
					logger.error(" - " + each);
				}
			}

			// Verify the values res names

			List<String> valuesResNamesPrefixErrors = ListUtils.newArrayList();

			dirsToVerify.each { File res ->

				res.listFiles().each {
					if (it.isDirectory() && it.name.startsWith("values")) {
						verifyValuesResNamePrefix(it, prefix, valuesResNamesPrefixErrors);
					}
				}
			}

			if (!valuesResNamesPrefixErrors.isEmpty()) {
				logger.error("The following values res names should start with the prefix: " + prefix);
				for (String each : valuesResNamesPrefixErrors) {
					logger.error(" - " + each);
				}
			}

			if (!filePrefixErrors.isEmpty() || !valuesResNamesPrefixErrors.isEmpty()) {
				throw new GradleException("Prefix verification failed");
			}
		}
	}

	protected void verifyFilePrefix(File dirFile, String prefix, List<String> errors) {
		List<String> extensionsToVerify = ListUtils.newArrayList(".xml", ".png");
		dirFile.listFiles().each { File file ->
			String extension = file.getName().replace(file.name.replaceFirst(~/\.[^\.]+$/, ''), "");
			if (!file.isDirectory() && extensionsToVerify.contains(extension) && !file.getName().startsWith(prefix)) {
				errors.add(file.absolutePath);
			}
		}
	}

	protected void verifyValuesResNamePrefix(File dirFile, String prefix, List<String> errors) {
		List<String> filesToIgnore = propertyResolver.getStringListProp('RESOURCE_FILES_TO_SKIP_PREFIX_VALIDATION', [])
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

		List<String> elementsToVerify = ListUtils.newArrayList("string", "plurals", "color", "dimen", "declare-styleable", "style", "bool");
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
