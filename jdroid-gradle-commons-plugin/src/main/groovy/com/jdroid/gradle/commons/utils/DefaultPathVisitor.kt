package com.jdroid.gradle.commons.utils

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

open class DefaultPathVisitor : FileVisitor<Path> {

    @Throws(IOException::class)
    override fun preVisitDirectory(path: Path, basicFileAttributes: BasicFileAttributes): FileVisitResult {
        return FileVisitResult.CONTINUE
    }

    @Throws(IOException::class)
    override fun visitFile(path: Path, basicFileAttributes: BasicFileAttributes): FileVisitResult {
        return FileVisitResult.CONTINUE
    }

    @Throws(IOException::class)
    override fun visitFileFailed(path: Path, e: IOException): FileVisitResult {
        return FileVisitResult.CONTINUE
    }

    @Throws(IOException::class)
    override fun postVisitDirectory(path: Path, e: IOException): FileVisitResult {
        return FileVisitResult.CONTINUE
    }
}
