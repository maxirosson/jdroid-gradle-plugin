package com.jdroid.gradle.commons;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
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
	public Boolean isKotlinEnabled;

	public void apply(Project project) {
		this.project = project;

		propertyResolver = new PropertyResolver(project);
		jdroid = project.getExtensions().create("jdroid", getExtensionClass(), project);

		isPublicationConfigurationEnabled = fetchIsPublicationConfigurationEnabled();
		if (isPublicationConfigurationEnabled) {

			// https://docs.gradle.org/current/userguide/publishing_overview.html
			// https://docs.gradle.org/current/userguide/publishing_maven.html
			if (!project.getPlugins().hasPlugin("maven-publish")) {
				applyPlugin("maven-publish");
			}

			// TODO Get this info from semantic version plugin
			boolean isSnapshot = project.getVersion().toString().endsWith("-SNAPSHOT");

			isSourcesPublicationEnabled = propertyResolver.getBooleanProp("SOURCES_PUBLICATION_ENABLED", false);
			isSigningPublicationEnabled = propertyResolver.getBooleanProp("SIGNING_PUBLICATION_ENABLED", false) && !isSnapshot;


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
									if (isSnapshot) {
										mavenArtifactRepository.setName("snapshotsMavenRepo");
										mavenArtifactRepository.setUrl(jdroid.getPublishingSnapshotsRepoUrl());
									} else {
										mavenArtifactRepository.setName("releasesMavenRepo");
										mavenArtifactRepository.setUrl(jdroid.getPublishingReleasesRepoUrl());
									}
									mavenArtifactRepository.credentials(new Action<PasswordCredentials>() {
										@Override
										public void execute(PasswordCredentials passwordCredentials) {
											passwordCredentials.setUsername(jdroid.getPublishingRepoUsername());
											passwordCredentials.setPassword(jdroid.getPublishingRepoPassword());
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

		if (propertyResolver.getBooleanProp("GRADLE_BUILD_SCAN_ENABLED", true)) {
			GroovyUtils.configureBuildScan(project, propertyResolver.getBooleanProp("GRADLE_BUILD_SCAN_PUBLISH_ALWAYS", false));
		}

		isKotlinEnabled = propertyResolver.getBooleanProp("KOTLIN_ENABLED", true);
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
