package com.jdroid.gradle.root.task

enum class ProjectConfig(val source: String, val target: String, val isStrict: Boolean) {

    CODE_STYLES_CONFIG("/.idea/codeStyles/codeStyleConfig.xml", true),
    CODE_STYLES_PROJECT("/.idea/codeStyles/Project.xml", true),
    INSPECTION_PROFILE("/.idea/inspectionProfiles/profile.xml", true),
    INSPECTION_PROFILES_PROFILES_SETTINGS("/.idea/inspectionProfiles/profiles_settings.xml", true),
    GIT_IGNORE("/gitignore", ".gitignore", false),
    CI_IGNORE("/.ciignore", false),
    EDITOR_CONFIG("/.editorconfig", true),
    GIT_HOOKS_COMMIT_MSG("/scripts/git/commit-msg", false),
    INIT_GIT_HOOKS("/scripts/git/init_git_hooks.sh", false),
    GRADLE_WRAPPER_PROPERTIES("/gradle/wrapper/gradle-wrapper.properties", true);

    constructor(source: String, strict: Boolean) : this(source, source, strict)
}
