package com.jdroid.gradle.commons

public class JavaBaseGradleExtension extends BaseGradleExtension {

	def integrationTestsPattern = "**/integration/**/*Test.class"

	public JavaBaseGradleExtension(JavaBaseGradlePlugin javaBaseGradlePlugin) {
		super(javaBaseGradlePlugin)
	}
}