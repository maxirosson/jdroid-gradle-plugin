package com.jdroid.gradle.commons.utils;

import java.util.List;

/**
 * This class contains functions for managing Strings
 */
public abstract class StringUtils {
	
	public final static String EMPTY = "";
	public final static String COMMA = ",";
	public final static String SPACE = " ";

	public static Boolean isEmpty(String text) {
		return text != null ? text.length() == 0 : true;
	}
	
	public static Boolean isNotEmpty(String text) {
		return !isEmpty(text);
	}
	
	public static List<String> splitToListWithCommaSeparator(String text) {
		return splitToList(text, COMMA);
	}

	public static List<String> splitToList(String text, String separator) {
		List<String> values = ListUtils.newArrayList();
		if (isNotEmpty(text)) {
			values = ListUtils.newArrayList(text.split(separator));
		}
		return values;
	}
}
