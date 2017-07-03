package com.jdroid.gradle.commons.tasks
import org.gradle.api.DefaultTask

public class AbstractIncrementVersionTask extends DefaultTask {

	protected def changeVersion(String versionType, Integer newVersion) {
		def file = project.file("./build.gradle")
		def patternVersionNumber = java.util.regex.Pattern.compile(versionType + " = (\\d+)")
		def buildGradleText = file.getText()
		def matcherVersionNumber = patternVersionNumber.matcher(buildGradleText)
		matcherVersionNumber.find()
		def currentVersion = Integer.parseInt(matcherVersionNumber.group(1))
		if (newVersion == null) {
			newVersion = currentVersion + 1
		}
		def fileContent = matcherVersionNumber.replaceAll(versionType + " = " + newVersion)
		file.write(fileContent)
		return newVersion
	}

	protected void commitVersionChange() {
		project.exec {
			commandLine 'git', 'diff', 'HEAD'
		}

		project.exec {
			commandLine 'git', 'add', '-A'
		}

		project.exec {
			commandLine 'git', '-c', 'commit.gpgsign=false', 'commit', '-m', "Changed version to v${project.version}"
		}
	}
}
