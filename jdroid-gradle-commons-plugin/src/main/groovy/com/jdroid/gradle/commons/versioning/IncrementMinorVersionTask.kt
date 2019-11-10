package com.jdroid.gradle.commons.versioning

open class IncrementMinorVersionTask : AbstractIncrementVersionTask() {

    init {
        description = "Increments the minor version (X.Y.Z) -> (X.Y+1.0)"
    }

    override fun incrementVersion(version: Version) {
        version.incrementMinor()
    }
}
