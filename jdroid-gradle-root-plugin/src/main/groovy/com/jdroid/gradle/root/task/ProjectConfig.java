package com.jdroid.gradle.root.task;

import org.gradle.api.Project;

public enum ProjectConfig {

	CODE_STYLES_CONFIG("/.idea/codeStyles/codeStyleConfig.xml"),
	CODE_STYLES_PROJECT("/.idea/codeStyles/Project.xml"),
	INSPECTION_PROFILE("/.idea/inspectionProfiles/profile.xml"),
	INSPECTION_PROFILES_PROFILES_SETTINGS("/.idea/inspectionProfiles/profiles_settings.xml"),
	GIT_IGNORE("/gitignore", ".gitignore"),
	CI_IGNORE("/.ciignore"),
	EDITOR_CONFIG("/.editorconfig"),
	GIT_HOOKS_PRE_COMMIT("/scripts/git/pre-commit"),
	GIT_HOOKS_COMMIT_MSG("/scripts/git/commit-msg"),
	INIT_GIT_HOOKS("/scripts/git/init_git_hooks.sh"),
	GRADLEW("/gradlew"),
	GRADLE_WRAPPER("/gradle/wrapper/gradle-wrapper.jar"),
	GRADLE_WRAPPER_PROPERTIES("/gradle/wrapper/gradle-wrapper.properties");

	private String source;
	private String target;

	ProjectConfig(String source) {
		this(source, source);
	}

	ProjectConfig(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	public Boolean isEnabled(Project project) {
		// TODO Add support to disable a project config sync through a property
		return true;
	}
}
