package com.jdroid.gradle.android.task

import com.jdroid.gradle.commons.tasks.AbstractTask
import org.gradle.api.GradleException

public class CopyBuildsTask extends AbstractTask {

	public CopyBuildsTask() {
		description = 'Copy the APKs/App Bundles on the build directory to the specified target directory'
	}

	@Override
	protected void onExecute() {

		String targetBuildsDirPath = propertyResolver.getStringProp('TARGET_BUILDS_DIR_PATH')
		if (targetBuildsDirPath == null) {
			throw new GradleException('Missing TARGET_BUILDS_DIR_PATH parameter')
		}

		project.copy {
			from ('./build/outputs/bundle') {
				include('**/*.aab')
				exclude('**/*unaligned.aab')
			}
			from ('./build/outputs/apk') {
				include('**/*.apk')
				exclude('**/*unaligned.apk')
			}
			into targetBuildsDirPath
		}

		log("Copied APKs/App Bundles to " + targetBuildsDirPath)
	}
}
