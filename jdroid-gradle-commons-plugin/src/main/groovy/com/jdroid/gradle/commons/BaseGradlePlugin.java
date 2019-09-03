package com.jdroid.gradle.commons;

import com.jdroid.gradle.commons.tasks.BuildScriptDependenciesTask;
import com.jdroid.gradle.commons.tasks.CloseGitHubMilestoneTask;
import com.jdroid.gradle.commons.tasks.CreateGitHubReleaseTask;
import com.jdroid.gradle.commons.tasks.GenerateChangelogTask;
import com.jdroid.gradle.commons.tasks.PrintVersionTask;
import com.jdroid.gradle.commons.versioning.IncrementMajorVersionTask;
import com.jdroid.gradle.commons.versioning.IncrementMinorVersionTask;
import com.jdroid.gradle.commons.versioning.IncrementPatchVersionTask;
import com.jdroid.gradle.commons.versioning.Version;

import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.DependencyResolveDetails;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPom;

import java.util.HashMap;
import java.util.Map;

public class BaseGradlePlugin implements Plugin<Project> {

	protected Project project;
	protected PropertyResolver propertyResolver;
	protected BaseGradleExtension jdroid;
	public Boolean isPublicationConfigurationEnabled;
	public Boolean isSourcesPublicationEnabled;
	public Boolean isSigningPublicationEnabled;
	protected String artifactId;

	public void apply(Project project) {
		this.project = project;

		propertyResolver = new PropertyResolver(project);
		jdroid = project.getExtensions().create("jdroid", getExtensionClass(), project);

		if (project.getVersion().equals(Project.DEFAULT_VERSION)) {
			project.setVersion(project.getRootProject().getVersion());
		}


		if (project.getVersion().equals(Project.DEFAULT_VERSION)) {
			project.getLogger().warn("Version not specified on project " + project.getName() + " or its root project. Assigned v0.1.0 as default version");
			project.setVersion("0.1.0");
		}


		String baseVersion = project.getVersion() instanceof Version ? ((Version)project.getVersion()).getBaseVersion() : project.getVersion().toString();
		project.setVersion(createVersion(baseVersion));

		PrintVersionTask printVersionTask = project.getTasks().create("printVersion", PrintVersionTask.class);

		IncrementMajorVersionTask incrementMajorVersionTask = project.getTasks().create("incrementMajorVersion", IncrementMajorVersionTask.class);
		IncrementMinorVersionTask incrementMinorVersionTask = project.getTasks().create("incrementMinorVersion", IncrementMinorVersionTask.class);
		IncrementPatchVersionTask incrementPatchVersionTask = project.getTasks().create("incrementPatchVersion", IncrementPatchVersionTask.class);
		CreateGitHubReleaseTask createGitHubReleaseTask = project.getTasks().create("createGitHubRelease", CreateGitHubReleaseTask.class);
		CloseGitHubMilestoneTask closeGitHubMilestoneTask = project.getTasks().create("closeGitHubMilestone", CloseGitHubMilestoneTask.class);
		GenerateChangelogTask generateChangelogTask = project.getTasks().create("generateChangelog", GenerateChangelogTask.class);
		BuildScriptDependenciesTask buildScriptDependenciesTask = project.getTasks().create("buildScriptDependencies", BuildScriptDependenciesTask.class);


		project.afterEvaluate(new Action<Project>() {
			public void execute(Project p) {
				printVersionTask.setLogLevel(jdroid.getLogLevel());
				incrementMajorVersionTask.setLogLevel(jdroid.getLogLevel());
				incrementMinorVersionTask.setLogLevel(jdroid.getLogLevel());
				incrementPatchVersionTask.setLogLevel(jdroid.getLogLevel());
				createGitHubReleaseTask.setLogLevel(jdroid.getLogLevel());
				closeGitHubMilestoneTask.setLogLevel(jdroid.getLogLevel());
				generateChangelogTask.setLogLevel(jdroid.getLogLevel());
				buildScriptDependenciesTask.setLogLevel(jdroid.getLogLevel());
			}
		});


		if (!propertyResolver.getBooleanProp("ACCEPT_SNAPSHOT_DEPENDENCIES", true)) {
			project.getConfigurations().all(new Action<Configuration>() {
				@Override
				public void execute(Configuration files) {
					files.getResolutionStrategy().eachDependency(new Action<DependencyResolveDetails>() {
						@Override
						public void execute(DependencyResolveDetails details) {
							if (details.getRequested().getVersion().endsWith("-SNAPSHOT")) {
								throw new GradleException("Found snapshot dependency: " + details.getRequested().getGroup() + ":" + details.getRequested().getName() + ":" + details.getRequested().getVersion());
							}

						}

					});
				}

			});
		}

		isPublicationConfigurationEnabled = fetchIsPublicationConfigurationEnabled();
		if (isPublicationConfigurationEnabled) {

			// https://docs.gradle.org/current/userguide/publishing_overview.html
			// https://docs.gradle.org/current/userguide/publishing_maven.html
			if (!project.getPlugins().hasPlugin("maven-publish")) {
				applyPlugin("maven-publish");
			}
			isSourcesPublicationEnabled = propertyResolver.getBooleanProp("SOURCES_PUBLICATION_ENABLED", false);
			isSigningPublicationEnabled = propertyResolver.getBooleanProp("SIGNING_PUBLICATION_ENABLED", false);


			Boolean localUpload = propertyResolver.getBooleanProp("LOCAL_UPLOAD", true);
			String localMavenRepo = propertyResolver.getStringProp("LOCAL_MAVEN_REPO");

			if (localUpload && localMavenRepo == null) {
				project.getLogger().warn("LOCAL_MAVEN_REPO property is not defined. Skipping publish configuration");
			} else {
				project.afterEvaluate(new Action<Project>() {
					@Override
					public void execute(Project project) {
						RepositoryHandler repositoryHandler = project.getExtensions().findByType(PublishingExtension.class).getRepositories();
						if (localUpload) {
							repositoryHandler.maven(new Action<MavenArtifactRepository>() {
								@Override
								public void execute(MavenArtifactRepository mavenArtifactRepository) {
									mavenArtifactRepository.setName("localMavenRepo");
									mavenArtifactRepository.setUrl(project.uri(localMavenRepo));
								}
							});
						} else {
							repositoryHandler.maven(new Action<MavenArtifactRepository>() {
								@Override
								public void execute(MavenArtifactRepository mavenArtifactRepository) {
									Boolean isSnapshot = ((Version)project.getVersion()).isSnapshot();
									if (isSnapshot == null || isSnapshot) {
										mavenArtifactRepository.setName("snapshotsMavenRepo");
										mavenArtifactRepository.setUrl(jdroid.getSnapshotsMavenRepo());
									} else {
										mavenArtifactRepository.setName("releasesMavenRepo");
										mavenArtifactRepository.setUrl(jdroid.getReleasesMavenRepo());
									}
									mavenArtifactRepository.credentials(new Action<PasswordCredentials>() {
										@Override
										public void execute(PasswordCredentials passwordCredentials) {
											passwordCredentials.setUsername(jdroid.getNexusUsername());
											passwordCredentials.setPassword(jdroid.getNexusPassword());
										}
									});
								}
							});
						}
					}
				});
			}
		}

		artifactId = propertyResolver.getStringProp("ARTIFACT_ID");
		if (artifactId == null) {
			artifactId =  new PropertyResolver(project.getRootProject()).getStringProp("ARTIFACT_ID");
			if (artifactId == null) {
				artifactId = project.getName();
			}
		}
	}

	protected Version createVersion(String version) {
		return new Version(project, version);
	}

	protected Class<? extends BaseGradleExtension> getExtensionClass() {
		return BaseGradleExtension.class;
	}

	protected boolean fetchIsPublicationConfigurationEnabled() {
		return propertyResolver.getBooleanProp("PUBLICATION_CONFIGURATION_ENABLED", false);
	}

	protected void addConfiguration(String configuration) {
		project.getConfigurations().create(configuration);
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
		Map<String, String> map = new HashMap<>();
		map.put("plugin", plugin);
		project.apply(map);
	}

	public BaseGradleExtension getExtension() {
		return jdroid;
	}

	protected String getPackaging() {
		return null;
	}

	protected Action<? super MavenPom> createMavenPom() {
		return new JdroidPom().createMavenPom(project, jdroid, artifactId, getPackaging());
	}
}
