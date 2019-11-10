package com.jdroid.gradle.commons

import org.gradle.api.Project

open class JavaBaseGradleExtension : BaseGradleExtension {

    var integrationTestsPattern = "**/integration/**/*Test.class"

    // TODO Remove this constructor. Only for testing
    constructor() : super()

    constructor(project: Project) : super(project)
}
