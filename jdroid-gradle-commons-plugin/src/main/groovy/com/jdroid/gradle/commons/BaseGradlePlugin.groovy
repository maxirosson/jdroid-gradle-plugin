package com.jdroid.gradle.commons

import com.jdroid.gradle.commons.tasks.IncrementMajorVersionTask
import com.jdroid.gradle.commons.tasks.IncrementMinorVersionTask
import com.jdroid.gradle.commons.tasks.IncrementPatchVersionTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

public class BaseGradlePlugin implements Plugin<Project> {

	protected Project project;

	protected jdroid

	public void apply(Project project) {
		this.project = project

		project.extensions.create("jdroid", getExtensionClass(), this)
		jdroid = project.jdroid

		if (project.version == Project.DEFAULT_VERSION) {
			project.version = project.rootProject.version
		}

		if (project.version == Project.DEFAULT_VERSION) {
			project.logger.warn("Version not specified on project ${project.name} or its root project. Assigned v0.1.0 as default version")
			project.version = "0.1.0"
		}

		String baseVersion = project.version instanceof Version ? ((Version)project.version).baseVersion : project.version.toString()
		project.version = createVersion(baseVersion)

		project.task('printVersion') {
			doLast {
				println project.version
			}
		}

		project.task('incrementMajorVersion', type: IncrementMajorVersionTask)
		project.task('incrementMinorVersion', type: IncrementMinorVersionTask)
		project.task('incrementPatchVersion', type: IncrementPatchVersionTask)

		project.task('buildScriptDependencies') {
			doLast {
				project.buildscript.configurations.classpath.asPath.split(':').each {
					println it
				}
			}
		}

		if (!jdroid.getBooleanProp('ACCEPT_SNAPSHOT_DEPENDENCIES', true)) {
			project.configurations.all {
				resolutionStrategy.eachDependency { details ->
					if (details.requested.version.endsWith("-SNAPSHOT")) {
						throw new GradleException("Found snapshot dependency: " + details.requested.group + ":" + details.requested.name + ":" + details.requested.version)
					}
				}
			}
		}
	}

	protected Version createVersion(String version) {
		return new Version(project, version)
	}

	protected Class<? extends BaseGradleExtension> getExtensionClass() {
		return BaseGradleExtension.class;
	}
}