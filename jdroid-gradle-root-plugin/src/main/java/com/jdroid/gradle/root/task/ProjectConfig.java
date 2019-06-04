package com.jdroid.gradle.root.task;

public enum ProjectConfig {

	CODE_STYLES_CONFIG("/.idea/codeStyles/codeStyleConfig.xml"),
	CODE_STYLES_PROJECT("/.idea/codeStyles/Project.xml"),
	INSPECTION_PROFILES_KTLINT("/.idea/inspectionProfiles/ktlint.xml"),
	INSPECTION_PROFILES_PROFILES_SETTINGS("/.idea/inspectionProfiles/profiles_settings.xml"),
	GIT_IGNORE("/gitignore", ".gitignore"),
	EDITOR_CONFIG("/.editorconfig"),
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
}
