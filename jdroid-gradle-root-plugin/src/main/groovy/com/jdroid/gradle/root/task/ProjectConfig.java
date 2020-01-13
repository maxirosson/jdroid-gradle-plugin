package com.jdroid.gradle.root.task;

public enum ProjectConfig {

	CODE_STYLES_CONFIG("/.idea/codeStyles/codeStyleConfig.xml", true),
	CODE_STYLES_PROJECT("/.idea/codeStyles/Project.xml", true),
	INSPECTION_PROFILE("/.idea/inspectionProfiles/profile.xml", true),
	INSPECTION_PROFILES_PROFILES_SETTINGS("/.idea/inspectionProfiles/profiles_settings.xml", true),
	GIT_IGNORE("/gitignore", ".gitignore", false),
	CI_IGNORE("/.ciignore", false),
	EDITOR_CONFIG("/.editorconfig", true),
	GIT_HOOKS_PRE_COMMIT("/scripts/git/pre-commit", false),
	GIT_HOOKS_COMMIT_MSG("/scripts/git/commit-msg", false),
	INIT_GIT_HOOKS("/scripts/git/init_git_hooks.sh", false),
	GRADLEW("/gradlew", true),
	GRADLE_WRAPPER("/gradle/wrapper/gradle-wrapper.jar", true),
	GRADLE_WRAPPER_PROPERTIES("/gradle/wrapper/gradle-wrapper.properties", true);

	private String source;
	private String target;
	private boolean strict;

	ProjectConfig(String source, boolean strict) {
		this(source, source, strict);
	}

	ProjectConfig(String source, String target, boolean strict) {
		this.source = source;
		this.target = target;
		this.strict = strict;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	public boolean isStrict() {
		return strict;
	}
}