package com.jdroid.gradle.commons.tasks;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.gradle.api.artifacts.Configuration;

public class BuildScriptDependenciesTask extends AbstractTask {

	@Override
	protected void onExecute() {
		Configuration classpathConfiguration = getProject().getBuildscript().getConfigurations().findByName("classpath");
		if (classpathConfiguration != null) {
			for (String each : classpathConfiguration.getAsPath().split(":")) {
				DefaultGroovyMethods.println(this, each);
			}
		}
	}

}
