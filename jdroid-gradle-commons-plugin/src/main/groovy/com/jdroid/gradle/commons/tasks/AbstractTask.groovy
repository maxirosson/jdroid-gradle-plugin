package com.jdroid.gradle.commons.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.process.ExecResult

public class AbstractTask extends DefaultTask {

	public ExecResult execute(def command, def workingDirectory, Boolean logStandardOutput, Boolean ignoreExitValueParam) {
		StringBuilder builder = new StringBuilder()
		command.each {
			builder.append(it)
			builder.append(" ")
		}
		println "Executing command: " + builder.toString()
		project.exec {
			workingDir workingDirectory
			commandLine command
			ignoreExitValue ignoreExitValueParam
			if (logStandardOutput) {
				standardOutput new LogOutputStream(logger, LogLevel.ERROR)
			}
			errorOutput new LogOutputStream(logger, LogLevel.ERROR)
		}
	}

	public ExecResult execute(def command, def workingDirectory) {
		execute(command, workingDirectory, true, false)
	}

	public ExecResult execute(def command) {
		execute(command, project.rootProject.projectDir)
	}
}
