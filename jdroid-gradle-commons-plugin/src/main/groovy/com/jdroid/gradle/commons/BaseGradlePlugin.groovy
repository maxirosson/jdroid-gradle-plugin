package com.jdroid.gradle.commons

import com.jdroid.gradle.commons.tasks.CreateGitHubReleaseTask
import com.jdroid.gradle.commons.versioning.IncrementMajorVersionTask
import com.jdroid.gradle.commons.versioning.IncrementMinorVersionTask
import com.jdroid.gradle.commons.versioning.IncrementPatchVersionTask
import com.jdroid.gradle.commons.versioning.Version
import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

public class BaseGradlePlugin implements Plugin<Project> {

	protected Project project;
	protected PropertyResolver propertyResolver;
	protected jdroid

	public void apply(Project project) {
		this.project = project

		propertyResolver = new PropertyResolver(project);
		project.extensions.create("jdroid", getExtensionClass(), project)
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

		IncrementMajorVersionTask incrementMajorVersionTask = project.task('incrementMajorVersion', type: IncrementMajorVersionTask)
		IncrementMinorVersionTask incrementMinorVersionTask = project.task('incrementMinorVersion', type: IncrementMinorVersionTask)
		IncrementPatchVersionTask incrementPatchVersionTask = project.task('incrementPatchVersion', type: IncrementPatchVersionTask)
		CreateGitHubReleaseTask createGitHubReleaseTask = project.task('createGitHubRelease', type: CreateGitHubReleaseTask)


		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				incrementMajorVersionTask.setLogLevel(project.jdroid.getLogLevel());
				incrementMinorVersionTask.setLogLevel(project.jdroid.getLogLevel());
				incrementPatchVersionTask.setLogLevel(project.jdroid.getLogLevel());
				createGitHubReleaseTask.setLogLevel(project.jdroid.getLogLevel());
			}
		});

		project.task('buildScriptDependencies') {
			doLast {
				project.buildscript.configurations.classpath.asPath.split(':').each {
					println it
				}
			}
		}

		if (!propertyResolver.getBooleanProp('ACCEPT_SNAPSHOT_DEPENDENCIES', true)) {
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