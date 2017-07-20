package com.jdroid.gradle.commons

import com.jdroid.gradle.commons.utils.TypeUtils
import org.gradle.api.Project

public class Version {

	Project project
	Integer versionMajor
	Integer versionMinor
	Integer versionPatch
	String versionClassifier
	Boolean isSnapshot
	String featureName
	String featureBranchPrefix
	Boolean isLocal

	public Version(Project project, String version) {

		this.project = project

		def versionSplit = version.split("\\.")
		if (versionSplit.length != 3) {
			throw new RuntimeException("The version [${version}] is not a valid Semantic Versioning")
		}
		versionMajor = TypeUtils.getInteger(versionSplit[0])
		if (versionMajor > 99 || versionMajor < 0) {
			throw new RuntimeException("The version major [${versionMajor}] should be a number between 0 and 99")
		}

		versionMinor = TypeUtils.getInteger(versionSplit[1])
		if (versionMinor > 99 || versionMinor < 0) {
			throw new RuntimeException("The version minor [${versionMinor}] should be a number between 0 and 99")
		}

		versionPatch = TypeUtils.getInteger(versionSplit[2])
		if (versionPatch > 99 || versionPatch < 0) {
			throw new RuntimeException("The version patch [${versionPatch}] should be a number between 0 and 99")
		}

		isSnapshot = project.jdroid.getBooleanProp('SNAPSHOT', true)
		isLocal = project.jdroid.getBooleanProp('LOCAL', false)
		featureBranchPrefix = project.jdroid.getStringProp('FEATURE_BRANCH_PREFIX', "feature/")

		versionClassifier = project.jdroid.getStringProp('VERSION_CLASSIFIER')
		if (versionClassifier == null) {

			String gitBranch = project.jdroid.getGitBranch()
			Boolean isFeatureBranch = gitBranch != null && gitBranch.startsWith(featureBranchPrefix)
			if (isFeatureBranch) {
				featureName = gitBranch.replace(featureBranchPrefix, "")
				versionClassifier = featureName
			}

			if (isLocal) {
				if (versionClassifier == null) {
					versionClassifier = ""
				} else {
					versionClassifier += "-"
				}
				versionClassifier += "LOCAL"
			}

			if (isSnapshot) {
				if (versionClassifier == null) {
					versionClassifier = ""
				} else {
					versionClassifier += "-"
				}
				versionClassifier += "SNAPSHOT"
			}
		}
	}

	public void incrementMajor() {
		versionMajor += 1
		versionMinor = 0
		versionPatch = 0
	}

	public void incrementMinor() {
		versionMinor += 1
		versionPatch = 0
	}

	public void incrementPatch() {
		versionPatch += 1
	}

	public String getBaseVersion() {
		return "${versionMajor}.${versionMinor}.${versionPatch}"
	}

	public String toString() {
		String versionName = getBaseVersion()
		if (versionClassifier != null && !versionClassifier.isEmpty()) {
			versionName += "-" + versionClassifier
		}
		return versionName;
	}
}