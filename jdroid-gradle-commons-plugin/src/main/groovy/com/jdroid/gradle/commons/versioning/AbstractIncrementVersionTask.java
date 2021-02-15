package com.jdroid.gradle.commons.versioning;

import com.jdroid.gradle.commons.CommandExecutor;
import com.jdroid.gradle.commons.tasks.AbstractTask;
import com.jdroid.gradle.commons.utils.ListUtils;
import com.jdroid.gradle.commons.utils.ProjectUtils;
import com.jdroid.java.utils.FileUtils;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class AbstractIncrementVersionTask extends AbstractTask {
	
	@Override
	protected void onExecute() {

		CommandExecutor commandExecutor = new CommandExecutor(getProject(), getLogLevel());

		File buildGradleFile = getProject().file(propertyResolver.getStringProp("VERSION_LOCATION_FILE", "./build.gradle"));
		final Pattern versionPattern = Pattern.compile("^\\s?version\\s?=\\s?[\"\'](\\d\\d?\\.\\d\\d?\\.\\d\\d?)[\"\']");

		List<String> lines = ListUtils.newArrayList();
		boolean versionFound = false;
		for (String line : FileUtils.readLines(buildGradleFile)) {
			if (!versionFound) {
				Matcher versionMatcher = versionPattern.matcher(line);
				if (versionMatcher.find()) {
					String versionText = versionMatcher.group(1);
					Version version = new Version(propertyResolver, ProjectUtils.getJdroidExtension(getProject()), versionText);
					incrementVersion(version);
					String newLineContent = versionMatcher.replaceFirst("version = '" + version.getBaseVersion() + "'");
					lines.add(newLineContent);
					getProject().setVersion(version.toString());
					versionFound = true;
				} else {
					lines.add(line);
				}
			} else {
				lines.add(line);
			}
		}
		
		if (versionFound) {
			
			FileUtils.writeLines(buildGradleFile, lines);

			configureGit();

			commandExecutor.execute("git diff HEAD");
			commandExecutor.execute("git add " + buildGradleFile.getAbsolutePath());
			commandExecutor.execute("git commit --no-gpg-sign -m \"Changed version to v" + new Version(getProject().getVersion().toString()).getBaseVersion() + "\"");

			Boolean versionIncrementPushEnabled = propertyResolver.getBooleanProp("VERSION_INCREMENT_PUSH_ENABLED", true);
			if (versionIncrementPushEnabled) {
				String versionIncrementBranch = propertyResolver.getStringProp("VERSION_INCREMENT_BRANCH");
				if (versionIncrementBranch != null) {
					commandExecutor.execute("git push origin HEAD:" + versionIncrementBranch);
				} else {
					commandExecutor.execute("git reset --soft HEAD~1");
					commandExecutor.execute("git add .");
					commandExecutor.execute("git stash");
					throw new RuntimeException("Missing VERSION_INCREMENT_BRANCH property. Reverting commit.");
				}
			}

		} else {
			throw new RuntimeException("Version not defined on " + buildGradleFile.getAbsolutePath());
		}

	}

	protected abstract void incrementVersion(Version version);

}
