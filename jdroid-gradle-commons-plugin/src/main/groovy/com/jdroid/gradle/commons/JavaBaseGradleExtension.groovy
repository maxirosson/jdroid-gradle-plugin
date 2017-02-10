package com.jdroid.gradle.commons

import com.jdroid.gradle.java.JavaGradlePlugin
import com.jdroid.java.exception.UnexpectedException
import org.gradle.api.Project

public class JavaBaseGradleExtension extends BaseGradleExtension {

	def integrationTestsPattern = "**/integration/**/*Test.class"

	public JavaBaseGradleExtension(JavaBaseGradlePlugin javaBaseGradlePlugin) {
		super(javaBaseGradlePlugin)
	}
}