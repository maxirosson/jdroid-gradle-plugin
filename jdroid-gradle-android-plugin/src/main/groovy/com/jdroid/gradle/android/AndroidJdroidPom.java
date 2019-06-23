package com.jdroid.gradle.android;

import com.jdroid.gradle.commons.JdroidPom;
import com.jdroid.java.collections.Maps;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.XmlProvider;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ExcludeRule;
import org.gradle.api.artifacts.ModuleDependency;
import org.gradle.api.publish.maven.MavenPom;

import java.util.List;
import java.util.Map;

import groovy.util.Node;

public class AndroidJdroidPom extends JdroidPom {

	private List<CharSequence> configurationNames;
	private Boolean debug;

	public AndroidJdroidPom(List<CharSequence> configurationNames, Boolean debug) {
		this.configurationNames = configurationNames;
		this.debug = debug;
	}

	@Override
	protected void configure(Project project, MavenPom mavenPom) {
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

						// TODO This is only working for jdroid
						String artifactId = dependency.getName();
						if (debug && dependency.getGroup().equals("com.jdroidtools") && artifactId.startsWith("jdroid-android-")) {
							artifactId = artifactId + "-debug";
						}
						dependencyNode.appendNode("groupId", dependency.getGroup());
						dependencyNode.appendNode("artifactId", artifactId);
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
