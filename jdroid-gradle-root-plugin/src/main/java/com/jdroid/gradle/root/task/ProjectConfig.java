package com.jdroid.gradle.root.task;

public enum ProjectConfig {

	CODE_STYLES_CONFIG("/.idea/codeStyles/codeStyleConfig.xml"),
	CODE_STYLES_PROJECT("/.idea/codeStyles/Project.xml"),
	INSPECTION_PROFILES("/.idea/inspectionProfiles/Project_Default.xml"),
	GIT_IGNORE("/gitignore", ".gitignore");

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
