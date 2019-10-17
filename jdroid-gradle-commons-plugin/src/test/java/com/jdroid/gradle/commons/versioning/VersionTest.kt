package com.jdroid.gradle.commons.versioning

import com.jdroid.gradle.commons.BaseGradleExtension
import com.jdroid.gradle.commons.PropertyResolver
import org.junit.Assert
import org.junit.Test

class VersionTest {

    @Test
    fun validVersion() {
        var version = createVersion("1.2.3")
        Assert.assertEquals(1, version.versionMajor)
        Assert.assertEquals(2, version.versionMinor)
        Assert.assertEquals(3, version.versionPatch)
        Assert.assertEquals("1.2.3", version.toString())

        version = createVersion("111.222.333")
        Assert.assertEquals(111, version.versionMajor)
        Assert.assertEquals(222, version.versionMinor)
        Assert.assertEquals(333, version.versionPatch)
        Assert.assertEquals("111.222.333", version.toString())
    }

    @Test(expected = RuntimeException::class)
    fun notSemanticVersion() {
        createVersion("1.2.3.4")
    }

    @Test(expected = RuntimeException::class)
    fun notSemanticVersion2() {
        createVersion("1.2")
    }

    @Test(expected = RuntimeException::class)
    fun notSemanticVersion3() {
        createVersion("1")
    }

    @Test(expected = RuntimeException::class)
    fun invalidMajor() {
        createVersion("1111.2.3")
    }

    @Test(expected = RuntimeException::class)
    fun invalidMinor() {
        createVersion("1.2222.3")
    }

    @Test(expected = RuntimeException::class)
    fun invalidPatch() {
        createVersion("1.2.3333")
    }

    @Test
    fun incrementMajor() {
        val version = createVersion("1.2.3")
        version.incrementMajor()
        Assert.assertEquals(2, version.versionMajor)
        Assert.assertEquals(0, version.versionMinor)
        Assert.assertEquals(0, version.versionPatch)
    }

    @Test(expected = RuntimeException::class)
    fun invalidIncrementMajor() {
        val version = createVersion("999.2.3")
        version.incrementMajor()
    }

    @Test
    fun incrementMinor() {
        var version = createVersion("1.2.3")
        version.incrementMinor()
        Assert.assertEquals(1, version.versionMajor)
        Assert.assertEquals(3, version.versionMinor)
        Assert.assertEquals(0, version.versionPatch)

        version = createVersion("1.999.3")
        version.incrementMinor()
        Assert.assertEquals(2, version.versionMajor)
        Assert.assertEquals(0, version.versionMinor)
        Assert.assertEquals(0, version.versionPatch)
    }

    @Test(expected = RuntimeException::class)
    fun invalidIncrementMinor() {
        val version = createVersion("999.999.3")
        version.incrementMinor()
    }

    @Test
    fun incrementPatch() {
        var version = createVersion("1.2.3")
        version.incrementPatch()
        Assert.assertEquals(1, version.versionMajor)
        Assert.assertEquals(2, version.versionMinor)
        Assert.assertEquals(4, version.versionPatch)

        version = createVersion("1.2.999")
        version.incrementPatch()
        Assert.assertEquals(1, version.versionMajor)
        Assert.assertEquals(3, version.versionMinor)
        Assert.assertEquals(0, version.versionPatch)
    }

    @Test(expected = RuntimeException::class)
    fun invalidIncrementPatch() {
        val version = createVersion("999.999.999")
        version.incrementPatch()
    }

    private fun createVersion(version: String): Version {
        val extension = BaseGradleExtension()
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
        return Version(propertyResolver, extension, version)
    }
}