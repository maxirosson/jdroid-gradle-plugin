package com.jdroid.gradle.commons

import com.jdroid.gradle.commons.tasks.CreateGitHubReleaseTask
import com.jdroid.gradle.commons.versioning.IncrementMajorVersionTask
import com.jdroid.gradle.commons.versioning.IncrementMinorVersionTask
import com.jdroid.gradle.commons.versioning.IncrementPatchVersionTask
import com.jdroid.gradle.commons.versioning.Version
import com.jdroid.java.collections.Maps
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
		BaseGradleExtension extension = project.getExtensions().create("jdroid", getExtensionClass(), project);
		jdroid = project.jdroid

		if (project.getVersion().equals(Project.DEFAULT_VERSION)) {
			project.setVersion(project.getRootProject().getVersion());
		}

		if (project.getVersion().equals(Project.DEFAULT_VERSION)) {
			project.getLogger().warn("Version not specified on project " + project.getName() + " or its root project. Assigned v0.1.0 as default version");
			project.setVersion("0.1.0");
		}

		String baseVersion = project.getVersion() instanceof Version ? ((Version)project.getVersion()).getBaseVersion() : project.getVersion().toString();
		project.setVersion(createVersion(baseVersion))

		project.task('printVersion') {
			doLast {
				println project.version
			}
		}

		IncrementMajorVersionTask incrementMajorVersionTask = project.getTasks().create("incrementMajorVersion", IncrementMajorVersionTask.class);
		IncrementMinorVersionTask incrementMinorVersionTask = project.getTasks().create("incrementMinorVersion", IncrementMinorVersionTask.class);
		IncrementPatchVersionTask incrementPatchVersionTask = project.getTasks().create("incrementPatchVersion", IncrementPatchVersionTask.class);
		CreateGitHubReleaseTask createGitHubReleaseTask = project.getTasks().create('createGitHubRelease', CreateGitHubReleaseTask.class);

		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				incrementMajorVersionTask.setLogLevel(extension.getLogLevel());
				incrementMinorVersionTask.setLogLevel(extension.getLogLevel());
				incrementPatchVersionTask.setLogLevel(extension.getLogLevel());
				createGitHubReleaseTask.setLogLevel(extension.getLogLevel());
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

	protected void addDependency(String configuration, String group, String name, String version) {
		project.getDependencies().add(configuration, group + ":" + name + ":" + version);
	}

	protected void addDependency(String configuration, String group, String name, String version, String classifier) {
		project.getDependencies().add(configuration, group + ":" + name + ":" + version + ":" + classifier);
	}

	protected void addDependency(String configuration, String group, String name, String version, String classifier, String extension) {
		project.getDependencies().add(configuration, group + ":" + name + ":" + version + ":" + classifier + "@" + extension);
	}

	protected void applyPlugin(String plugin) {
		Map<String, String> map = Maps.newHashMap();
		map.put("plugin", plugin);
		project.apply(map);
	}
}