package com.jdroid.gradle.android.task

import com.jdroid.gradle.commons.tasks.AbstractTask
import org.gradle.api.GradleException

public class CopyApksTask extends AbstractTask {

	public CopyApksTask() {
		description = 'Copy the APKs on the build directory to the specified target directory'
	}

	@Override
	protected void onExecute() {

		String targetApksDirPath = propertyResolver.getStringProp('TARGET_APKS_DIR_PATH')
		if (targetApksDirPath == null) {
			throw new GradleException('Missing TARGET_APKS_DIR_PATH parameter')
		}

		project.copy {
			from './build/outputs/apk'
			into targetApksDirPath
			include('**/*.apk')
			exclude('**/*unaligned.apk')
		}

		log("Copied APKs to " + targetApksDirPath)
	}
}
