package com.jdroid.gradle.android.versioning

import com.jdroid.gradle.android.AndroidGradlePluginExtension
import com.jdroid.gradle.commons.PropertyResolver
import org.junit.Assert
import org.junit.Test

class AndroidVersionTest {

    @Test
    fun validVersion() {
        val version = createVersion("1.2.3")
        Assert.assertEquals(210010203, version.versionCode)
    }

    private fun createVersion(version: String): AndroidVersion {
        val extension = AndroidGradlePluginExtension()
        extension.minimumSdkVersion = 21
        val propertyResolver = object : PropertyResolver(null) {
            override fun getIntegerProp(propertyName: String, defaultValue: Int?): Int? {
                return defaultValue
            }

            override fun getBooleanProp(propertyName: String?, defaultValue: Boolean?): Boolean {
                return defaultValue!!
            }

            override fun getStringProp(propertyName: String?, defaultValue: String?): String {
                return defaultValue!!
            }

            override fun getStringProp(propertyName: String?): String {
                return ""
            }
        }
        return AndroidVersion(propertyResolver, extension, version)
    }
}