package com.jdroid.gradle.root.config;

import com.jdroid.gradle.commons.tasks.AbstractTask
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

// Install graphviz tool in order to run this task. On Mac: brew install graphviz
// https://github.com/JakeWharton/SdkSearch/blob/master/gradle/projectDependencyGraph.gradle
public class ProjectDependencyGraphTask extends AbstractTask {

	@Override
	protected void onExecute() {

		def dot = new File(project.rootProject.buildDir, 'reports/dependency-graph/project.dot')
		dot.parentFile.mkdirs()
		dot.delete()

		dot << 'digraph {\n'
		dot << "  graph [label=\"${project.rootProject.name}\\n \",labelloc=t,fontsize=30,ranksep=1.4];\n"
		dot << '  node [style=filled, fillcolor="#bbbbbb"];\n'
		dot << '  rankdir=TB;\n'

		def rootProjects = []
		def queue = [project.rootProject]
		while (!queue.isEmpty()) {
			def project = queue.remove(0)
			rootProjects.add(project)
			queue.addAll(project.childProjects.values())
		}

		def projects = new LinkedHashSet<Project>()
		def dependencies = new LinkedHashMap<Tuple2<Project, Project>, List<String>>()
		def multiplatformProjects = []
		def jsProjects = []
		def androidProjects = []
		def javaProjects = []

		queue = [project.rootProject]
		while (!queue.isEmpty()) {
			def project = queue.remove(0)

			if (includeProject(project.name)) {
				queue.addAll(project.childProjects.values())

				if (project.plugins.hasPlugin('org.jetbrains.kotlin.multiplatform')) {
					multiplatformProjects.add(project)
				}
				if (project.plugins.hasPlugin('kotlin2js')) {
					jsProjects.add(project)
				}
				if (project.plugins.hasPlugin('com.android.library') || project.plugins.hasPlugin('com.android.application')) {
					androidProjects.add(project)
				}
				if (project.plugins.hasPlugin('java-library') || project.plugins.hasPlugin('java')) {
					javaProjects.add(project)
				}

				project.configurations.all { config ->
					config.dependencies
						.withType(ProjectDependency)
						.collect { it.dependencyProject }
						.each { dependency ->
							if (includeProject(dependency.name)) {
								projects.add(project)
								projects.add(dependency)
								rootProjects.remove(dependency)

								def graphKey = new Tuple2<Project, Project>(project, dependency)
								def traits = dependencies.computeIfAbsent(graphKey) { new ArrayList<String>() }

								if (config.name.toLowerCase().endsWith('implementation')) {
									traits.add('style=dotted')
								}
							}
						}
				}
			}
		}

		projects = projects.sort { it.path }

		dot << '\n  # Projects\n\n'
		for (project in projects) {
			def traits = []

			if (rootProjects.contains(project)) {
				traits.add('shape=box')
			}

			if (multiplatformProjects.contains(project)) {
				traits.add('fillcolor="#ffd2b3"')
			} else if (jsProjects.contains(project)) {
				traits.add('fillcolor="#ffffba"')
			} else if (androidProjects.contains(project)) {
				traits.add('fillcolor="#baffc9"')
			} else if (javaProjects.contains(project)) {
				traits.add('fillcolor="#ffb3ba"')
			} else {
				traits.add('fillcolor="#eeeeee"')
			}

			dot << "  \"${project.path}\" [${traits.join(", ")}];\n"
		}

		dot << '\n  {rank = same;'
		for (project in projects) {
			if (rootProjects.contains(project)) {
				dot << " \"${project.path}\";"
			}
		}
		dot << '}\n'

		dot << '\n  # Dependencies\n\n'
		dependencies.forEach { key, traits ->
			dot << "  \"${key.first.path}\" -> \"${key.second.path}\""
			if (!traits.isEmpty()) {
				dot << " [${traits.join(", ")}]"
			}
			dot << '\n'
		}

		dot << '}\n'

		def p = 'dot -Tpng -O project.dot'.execute(null, dot.parentFile)
		p.waitFor()
		if (p.exitValue() != 0) {
			throw new RuntimeException(p.errorStream.text)
		}

		println("Project module dependency graph created at ${dot.absolutePath}.png")

	}

	// TODO Add extension support here
	private static boolean includeProject(String name) {
		return true
	}
}
