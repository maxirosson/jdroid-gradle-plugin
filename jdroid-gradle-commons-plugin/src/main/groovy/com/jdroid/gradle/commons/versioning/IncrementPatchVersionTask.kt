package com.jdroid.gradle.commons.versioning

open class IncrementPatchVersionTask : AbstractIncrementVersionTask() {

    init {
        description = "Increments the patch version (X.Y.Z) -> (X.Y.Z+1)"
    }

    override fun incrementVersion(version: Version) {
        version.incrementPatch()
    }
}
