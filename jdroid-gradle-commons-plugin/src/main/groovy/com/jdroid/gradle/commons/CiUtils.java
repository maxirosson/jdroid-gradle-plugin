package com.jdroid.gradle.commons;

class CiUtils {

	public static Boolean isCi() {
		return "true".equals(System.getenv("CI"));
	}
}
