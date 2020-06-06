package com.jdroid.gradle.commons;

class CiUtils {

	public static Boolean isCi() {
		return System.getenv("CI").equals("true");
	}
}
