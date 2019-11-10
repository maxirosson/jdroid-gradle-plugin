package com.jdroid.gradle.commons.versioning

open class IncrementMajorVersionTask : AbstractIncrementVersionTask() {

    init {
        description = "Increments the major version (X.Y.Z) -> (X+1.0.0)"
    }

    override fun incrementVersion(version: Version) {
        version.incrementMajor()
    }
}
