package com.jdroid.gradle.android.task

import com.jdroid.gradle.commons.tasks.AbstractTask
import org.gradle.api.GradleException

public class CopyAppBundlesTask extends AbstractTask {

	public CopyAppBundlesTask() {
		description = 'Copy the App Bundles on the build directory to the specified target directory'
	}

	@Override
	protected void onExecute() {

		String targetAppBundlesDirPath = propertyResolver.getStringProp('TARGET_APP_BUNDLES_DIR_PATH')
		if (targetAppBundlesDirPath == null) {
			throw new GradleException('Missing TARGET_APP_BUNDLES_DIR_PATH parameter')
		}

		project.copy {
			from './build/outputs/bundle'
			into targetAppBundlesDirPath
			include('**/*.aab')
			exclude('**/*unaligned.aab')
		}

		log("Copied App Bundles to " + targetAppBundlesDirPath)
	}
}
