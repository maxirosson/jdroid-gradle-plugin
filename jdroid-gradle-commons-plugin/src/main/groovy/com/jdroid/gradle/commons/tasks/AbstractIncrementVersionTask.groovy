package com.jdroid.gradle.commons.tasks

import com.jdroid.gradle.commons.Version
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.util.regex.Matcher
import java.util.regex.Pattern

public abstract class AbstractIncrementVersionTask extends DefaultTask {

	@TaskAction
	public void doExecute() {
		File buildGradleFile = project.file(project.jdroid.getStringProp("VERSION_LOCATION_FILE", "./build.gradle"))
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
			commitVersionChange()
		} else {
			throw new RuntimeException("Version not defined on " + buildGradleFile.absolutePath)
		}
	}

	protected abstract void incrementVersion(Version version);

	protected void commitVersionChange() {
		project.exec {
			commandLine 'git', 'diff', 'HEAD'
		}

		project.exec {
			commandLine 'git', 'add', '-A'
		}

		project.exec {
			commandLine 'git', '-c', 'commit.gpgsign=false', 'commit', '-m', "Changed version to v${project.version.baseVersion}"
		}
	}
}
