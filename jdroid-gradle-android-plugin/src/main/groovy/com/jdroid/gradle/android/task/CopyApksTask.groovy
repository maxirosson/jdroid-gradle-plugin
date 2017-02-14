package com.jdroid.gradle.android.task
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

public class CopyApksTask extends DefaultTask {

	public CopyApksTask() {
		description = 'Copy the APKs on the build directory to the specified target directory'
	}

	@TaskAction
	public void doExecute() {

		String targetApksDirPath = project.jdroid.getStringProp('TARGET_APKS_DIR_PATH')
		if (targetApksDirPath == null) {
			throw new GradleException('Missing TARGET_APKS_DIR_PATH parameter')
		}

		project.copy {
			from './build/outputs/apk'
			into targetApksDirPath
			include('**/*.apk')
			exclude('**/*unaligned.apk')
		}

		logger.info("Copied APKs to " + targetApksDirPath)
	}
}
