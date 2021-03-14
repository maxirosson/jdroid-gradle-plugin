package com.jdroid.gradle.root;

import com.jdroid.gradle.commons.BaseGradlePlugin;
import com.jdroid.gradle.commons.GroovyUtils;
import com.jdroid.gradle.commons.utils.ListUtils;
import com.jdroid.gradle.commons.utils.StringUtils;
import com.jdroid.gradle.root.config.ProjectConfigSyncTask;
import com.jdroid.gradle.root.config.ProjectConfigValidationTask;
import com.jdroid.gradle.root.config.ProjectDependencyGraphTask;
import com.releaseshub.gradle.plugin.ReleasesHubGradlePluginExtension;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.JavaExec;

import java.util.ArrayList;
import java.util.List;

import io.codearte.gradle.nexus.NexusStagingExtension;

public class RootProjectPlugin extends BaseGradlePlugin {

	// TODO This version should be defined on Libs/BuildLibs.kt
	private static final String KTLINT_VERSION = "0.36.0";

	public Boolean isKtLintEnabled;

	public void apply(Project project) {
		super.apply(project);

		ProjectConfigSyncTask projectConfigSyncTask = project.getTasks().create("syncProjectConfig", ProjectConfigSyncTask.class);
		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				projectConfigSyncTask.setLogLevel(getExtension().getLogLevel());
			}
		});

		ProjectConfigValidationTask projectConfigValidationTask = project.getTasks().create("checkProjectConfig", ProjectConfigValidationTask.class);
		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				projectConfigValidationTask.setLogLevel(getExtension().getLogLevel());
			}
		});

		if (propertyResolver.getBooleanProp("RELEASES_HUB_GRADLE_PLUGIN_ENABLED", true)) {
			applyPlugin("com.dipien.releaseshub.gradle.plugin");
			ReleasesHubGradlePluginExtension releasesHubGradlePluginExtension = project.getExtensions().getByType(ReleasesHubGradlePluginExtension.class);
			releasesHubGradlePluginExtension.setGitHubRepositoryOwner(getExtension().getGitHubRepositoryOwner());
			releasesHubGradlePluginExtension.setGitHubRepositoryName(getExtension().getGitHubRepositoryName());
			releasesHubGradlePluginExtension.setGitHubUserEmail(getExtension().getGitHubUserEmail());
			releasesHubGradlePluginExtension.setGitHubUserName(getExtension().getGitHubUserName());
			releasesHubGradlePluginExtension.setGitHubWriteToken(getExtension().getGitHubWriteToken());
			releasesHubGradlePluginExtension.setUserToken(getExtension().getReleasesHubUserToken());
			releasesHubGradlePluginExtension.setExcludes(ListUtils.newArrayList("gradle"));
		}

		isKtLintEnabled = propertyResolver.getBooleanProp("KTLINT_ENABLED", isKotlinEnabled);

		if (isKtLintEnabled && isKtLintEnabled) {
			configureKtlint();
		}

		ProjectDependencyGraphTask projectDependenciesGraph = project.getTasks().create("projectDependencyGraph", ProjectDependencyGraphTask.class);
		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				projectDependenciesGraph.setLogLevel(getExtension().getLogLevel());
			}
		});

		GroovyUtils.configureGradleWrapper(project);

		if (isPublicationConfigurationEnabled) {
			if (propertyResolver.getBooleanProp("NEXUS_STAGING_PLUGIN_ENABLED", true)) {
				applyPlugin("io.codearte.nexus-staging");
				NexusStagingExtension extension = project.getExtensions().getByType(NexusStagingExtension.class);
				extension.setUsername(jdroid.getPublishingRepoUsername());
				extension.setPassword(jdroid.getPublishingRepoPassword());
				extension.setStagingProfileId(jdroid.getPublishingStagingProfileId());
			}
		}
	}

	private void configureKtlint() {

		addConfiguration("ktlint");
		addDependency("ktlint", "com.pinterest", "ktlint", KTLINT_VERSION);

		String includesString = propertyResolver.getStringProp("KTLINT_INCLUDES");
		final List<String> includes = new ArrayList<>();
		if (includesString == null) {
			for(Project each : project.getSubprojects()) {
				includes.add(each.getName() + "/src/**/*.kt");
				includes.add(each.getName() + "/*.kts");
			}
			includes.add("buildSrc/src/**/*.kt");
			includes.add("buildSrc/*.kts");
		} else {
			includes.addAll(StringUtils.splitToList(includesString, "\n"));
		}

		Task ktlintTask = project.getTasks().create("ktlint", JavaExec.class, new Action<JavaExec>() {
			@Override
			public void execute(JavaExec javaExec) {
				javaExec.setDescription("Check Kotlin code style.");
				javaExec.setMain("com.pinterest.ktlint.Main");
				javaExec.setClasspath(project.getConfigurations().findByName("ktlint"));

				// to generate report in checkstyle format prepend following args:
				// "--reporter=plain", "--reporter=checkstyle,output=${buildDir}/ktlint.xml"
				List<String> args = new ArrayList<>();
				args.add("-a");
				args.addAll(includes);
				javaExec.setArgs(args);
			}
		});
		ktlintTask.setGroup("verification");

		Task ktlintFormatTask = project.getTasks().create("ktlintFormat", JavaExec.class, new Action<JavaExec>() {
			@Override
			public void execute(JavaExec javaExec) {
				javaExec.setDescription("Fix Kotlin code style deviations.");
				javaExec.setMain("com.pinterest.ktlint.Main");
				javaExec.setClasspath(project.getConfigurations().findByName("ktlint"));
				List<String> args = new ArrayList<>();
				args.add("-a");
				args.add("-F");
				args.addAll(includes);
				javaExec.setArgs(args);
			}
		});
		ktlintFormatTask.setGroup("formatting");
	}

	protected Class<? extends RootProjectExtension> getExtensionClass() {
		return RootProjectExtension.class;
	}

}
