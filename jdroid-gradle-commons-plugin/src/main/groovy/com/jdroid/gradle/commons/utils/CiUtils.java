package com.jdroid.gradle.commons.utils;

public class CiUtils {

	public static Boolean isCi() {
		return "true".equals(System.getenv("CI"));
	}
}
