package com.jdroid.gradle.commons;

import com.jdroid.java.collections.Maps;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.XmlProvider;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ExcludeRule;
import org.gradle.api.artifacts.ModuleDependency;
import org.gradle.api.publish.maven.MavenPom;
import org.gradle.api.publish.maven.MavenPomDeveloper;
import org.gradle.api.publish.maven.MavenPomDeveloperSpec;
import org.gradle.api.publish.maven.MavenPomLicense;
import org.gradle.api.publish.maven.MavenPomLicenseSpec;
import org.gradle.api.publish.maven.MavenPomOrganization;

import java.util.List;
import java.util.Map;

import groovy.util.Node;

public class JdroidPom {

	public static Action<? super MavenPom> createMavenPom(Project project, String artifactId, String artifactPackaging) {
		return createMavenPom(project, artifactId, artifactPackaging, null);
	}

	public static Action<? super MavenPom> createMavenPom(Project project, String artifactId, String artifactPackaging, List<String> configurationNames) {
		return new Action<MavenPom>() {
			@Override
			public void execute(MavenPom mavenPom) {
				mavenPom.getName().set(artifactId);
				mavenPom.getDescription().set(project.getDescription() != null ? project.getDescription() : project.getRootProject().getDescription());

				// TODO Not working
				mavenPom.setPackaging(artifactPackaging);

				mavenPom.getUrl().set("https://jdroidtools.com");
				mavenPom.getInceptionYear().set("2011");
				mavenPom.organization(new Action<MavenPomOrganization>() {
					@Override
					public void execute(MavenPomOrganization mavenPomOrganization) {
						mavenPomOrganization.getName().set("Jdroid");
						mavenPomOrganization.getUrl().set("https://jdroidtools.com");
					}

				});
				mavenPom.licenses(new Action<MavenPomLicenseSpec>() {
					@Override
					public void execute(MavenPomLicenseSpec mavenPomLicenseSpec) {
						mavenPomLicenseSpec.license(new Action<MavenPomLicense>() {
							@Override
							public void execute(MavenPomLicense mavenPomLicense) {
								mavenPomLicense.getName().set("The Apache License, Version 2.0");
								mavenPomLicense.getUrl().set("http://www.apache.org/licenses/LICENSE-2.0.txt");
								mavenPomLicense.getDistribution().set("repo");
							}

						});
					}

				});
				mavenPom.developers(new Action<MavenPomDeveloperSpec>() {
					@Override
					public void execute(MavenPomDeveloperSpec mavenPomDeveloperSpec) {
						mavenPomDeveloperSpec.developer(new Action<MavenPomDeveloper>() {
							@Override
							public void execute(MavenPomDeveloper mavenPomDeveloper) {
								mavenPomDeveloper.getName().set("Maxi Rosson");
								mavenPomDeveloper.getEmail().set("contact@jdroidtools.com");
							}

						});
					}

				});
				// TODO
//				mavenPom.scm(new Action<MavenPomScm>() {
//					@Override
//					public void execute(MavenPomScm mavenPomScm) {
//						mavenPomScm.getConnection().set("scm:git:" + jdroidComponentBuilder.getRepositorySshUrl());
//						mavenPomScm.getDeveloperConnection().set("scm:git:" + jdroidComponentBuilder.getRepositorySshUrl());
//						mavenPomScm.getUrl().set(jdroidComponentBuilder.getRepositorySshUrl());
//					}
//
//				});
//				mavenPom.issueManagement(new Action<MavenPomIssueManagement>() {
//					@Override
//					public void execute(MavenPomIssueManagement mavenPomIssueManagement) {
//						mavenPomIssueManagement.getSystem().set("GitHub");
//						mavenPomIssueManagement.getUrl().set(jdroidComponentBuilder.getRepositoryUrl() + "/issues");
//					}
//
//				});
				if (configurationNames != null) {
					mavenPom.withXml(new Action<XmlProvider>() {
						@Override
						public void execute(XmlProvider xmlProvider) {
							// Creating additional node for dependencies
							Node dependenciesNode = xmlProvider.asNode().appendNode("dependencies");

							Map<String, Dependency> dependenciesMap = Maps.newLinkedHashMap();
							for (CharSequence configurationName : configurationNames) {
								for (Dependency dependency : project.getConfigurations().getByName(configurationName.toString()).getAllDependencies()) {
									dependenciesMap.put(dependency.getGroup() + ":" + dependency.getName(), dependency);
								}
							}

							for (Dependency dependency : dependenciesMap.values()) {
								if (dependency.getGroup() != null) {
									Node dependencyNode = dependenciesNode.appendNode("dependency");
									dependencyNode.appendNode("groupId", dependency.getGroup());
									dependencyNode.appendNode("artifactId", dependency.getName());
									dependencyNode.appendNode("version", dependency.getVersion());

									// If there are any exclusions in dependency
									if (dependency instanceof ModuleDependency) {
										ModuleDependency moduleDependency = (ModuleDependency)dependency;
										if (moduleDependency.getExcludeRules().size() > 0) {
											Node exclusionsNode = dependencyNode.appendNode("exclusions");
											for (ExcludeRule excludeRule : moduleDependency.getExcludeRules()) {
												Node exclusionNode = exclusionsNode.appendNode("exclusion");
												if (excludeRule.getGroup() != null) {
													exclusionNode.appendNode("groupId", excludeRule.getGroup());
												}
												if (excludeRule.getModule() != null) {
													exclusionNode.appendNode("artifactId", excludeRule.getModule());
												}
											}
										}
									}
								}
							}
						}
					});
				}
			}

		};
	}

}
