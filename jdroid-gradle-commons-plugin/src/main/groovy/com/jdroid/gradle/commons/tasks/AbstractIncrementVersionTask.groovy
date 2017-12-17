package com.jdroid.gradle.commons.tasks

import com.jdroid.gradle.commons.CommandExecutor
import com.jdroid.gradle.commons.Version

import java.util.regex.Matcher
import java.util.regex.Pattern

public abstract class AbstractIncrementVersionTask extends AbstractTask {

	@Override
	protected void onExecute() {

		CommandExecutor commandExecutor = new CommandExecutor(getProject(), getLogLevel());

		File buildGradleFile = project.file(propertyResolver.getStringProp("VERSION_LOCATION_FILE", "./build.gradle"))
		Pattern versionPattern = Pattern.compile('^\\s?version\\s?=\\s?["\'](\\d\\d?\\.\\d\\d?\\.\\d\\d?)["\']')

		Boolean versionFound = false
		List lines = []
		buildGradleFile.eachLine { String line ->
			if (!versionFound) {
				Matcher versionMatcher = versionPattern.matcher(line)
				if (versionMatcher.find()) {
					String versionText = versionMatcher.group(1)
					Version version = new Version(project, versionText)
					incrementVersion(version)
					String newLineContent = versionMatcher.replaceFirst("version = '" + version.baseVersion + "'")
					lines.add(newLineContent)
					project.version = version
					versionFound = true
				} else {
					lines.add(line)
				}
			} else {
				lines.add(line)
			}
		}

		if (versionFound) {
			buildGradleFile.withWriter { out ->
				lines.each {
					out.println it
				}
			}

			String ciGithubUserName = propertyResolver.getStringProp("CI_GITHUB_USER_NAME")
			if (ciGithubUserName != null) {
				commandExecutor.execute('git config user.name ' + ciGithubUserName)
			}
			String ciGithubUserEmail = propertyResolver.getStringProp("CI_GITHUB_USER_EMAIL")
			if (ciGithubUserEmail != null) {
				commandExecutor.execute('git config user.email ' + ciGithubUserEmail)
			}
			commandExecutor.execute('git diff HEAD')
			commandExecutor.execute('git add ' + buildGradleFile.absolutePath)
			commandExecutor.execute('git commit --no-gpg-sign -m "Changed version to v${project.version.baseVersion}"')

			Boolean versionIncrementPushEnabled = propertyResolver.getBooleanProp("VERSION_INCREMENT_PUSH_ENABLED", true)
			if (versionIncrementPushEnabled) {
				String versionIncrementBranch = propertyResolver.getStringProp("VERSION_INCREMENT_BRANCH")
				if (versionIncrementBranch != null) {
					commandExecutor.execute('git push origin "HEAD:${versionIncrementBranch}"')
				} else {
					commandExecutor.execute('git reset --soft HEAD~1')
					commandExecutor.execute('git add .')
					commandExecutor.execute('git stash')
					throw new RuntimeException("Missing VERSION_INCREMENT_BRANCH property. Reverting commit.")
				}
			}
		} else {
			throw new RuntimeException("Version not defined on " + buildGradleFile.absolutePath)
		}
	}

	protected abstract void incrementVersion(Version version);

}
