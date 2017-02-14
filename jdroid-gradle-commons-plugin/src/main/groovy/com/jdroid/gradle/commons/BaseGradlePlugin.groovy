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
		project.version = jdroid.generateVersionName()

		project.task('printVersion') << {
			println project.version
		}

		project.task('incrementMajorVersion', type: IncrementMajorVersionTask)
		project.task('incrementMinorVersion', type: IncrementMinorVersionTask)
		project.task('incrementPatchVersion', type: IncrementPatchVersionTask)

		project.task('buildScriptDependencies') << {
			project.buildscript.configurations.classpath.asPath.split(':').each {
				println it
			}
		}

		if (jdroid.getBooleanProp('NO_SNAPSHOT_VALIDATION_ENABLED', false)) {
			project.configurations.all {
				resolutionStrategy.eachDependency { details ->
					if (details.requested.version.endsWith("-SNAPSHOT")) {
						throw new GradleException("Found snapshot dependency: " + details.requested.group + ":" + details.requested.name + ":" + details.requested.version)
					}
				}
			}
		}
	}

	protected Class<? extends BaseGradleExtension> getExtensionClass() {
		return BaseGradleExtension.class;
	}
}