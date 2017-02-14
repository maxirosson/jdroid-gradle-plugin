package com.jdroid.gradle.commons.utils;

import com.jdroid.java.collections.Lists;

import java.util.Collection;
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
		return Lists.newArrayList(splitToCollection(text, COMMA));
	}

	public static Collection<String> splitToCollection(String text, String separator) {
		Collection<String> values = Lists.newArrayList();
		if (isNotEmpty(text)) {
			values = Lists.newArrayList(text.split(separator));
		}
		return values;
	}
}
