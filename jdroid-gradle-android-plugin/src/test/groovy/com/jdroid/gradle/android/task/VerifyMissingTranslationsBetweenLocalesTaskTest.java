package com.jdroid.gradle.android.task;

import org.junit.Assert;
import org.junit.Test;

public class VerifyMissingTranslationsBetweenLocalesTaskTest {
	@Test
	public void getKey() {

		assertResKey(" <string name=\"key\">value</string>");
		assertResKey("<string name=\"key\">value</string>");
		assertResKey("<string name=\"key\">value</string> ");
		assertResKey("<string-array name=\"key\">");
		assertNullResKey("<item>0</item>");
		assertResKey("<plurals name=\"key\">");
		assertNullResKey("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		assertNullResKey("<resources>");
		assertNullResKey("<item quantity=\"one\">1 pago</item>");
		assertNullResKey("<!-- Menu -->");
		assertNullResKey("<!--<string name=\"key\">value</string>-->");

	}

	private void assertResKey(String fileLine) {
		Assert.assertEquals("key", VerifyMissingTranslationsBetweenLocalesTask.getKey(fileLine));
	}

	private void assertNullResKey(String fileLine) {
		Assert.assertNull(VerifyMissingTranslationsBetweenLocalesTask.getKey(fileLine));
	}

}
