package com.jdroid.gradle.android.task

import org.junit.Assert
import org.junit.Test

class VerifyMissingTranslationsBetweenLocalesTaskTest {
    @Test
    fun getKey() {

        assertResKey(" <string name=\"key\">value</string>")
        assertResKey("<string name=\"key\">value</string>")
        assertResKey("<string name=\"key\">value</string> ")
        assertResKey("<string-array name=\"key\">")
        assertNullResKey("<item>0</item>")
        assertResKey("<plurals name=\"key\">")
        assertNullResKey("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
        assertNullResKey("<resources>")
        assertNullResKey("<item quantity=\"one\">1 pago</item>")
        assertNullResKey("<!-- Menu -->")
        assertNullResKey("<!--<string name=\"key\">value</string>-->")
    }

    private fun assertResKey(fileLine: String) {
        Assert.assertEquals("key", VerifyMissingTranslationsBetweenLocalesTask.getKey(fileLine))
    }

    private fun assertNullResKey(fileLine: String) {
        Assert.assertNull(VerifyMissingTranslationsBetweenLocalesTask.getKey(fileLine))
    }
}
