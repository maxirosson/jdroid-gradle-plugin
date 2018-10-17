package com.jdroid.gradle.android.task;

import com.jdroid.gradle.commons.tasks.AbstractTask;
import com.jdroid.gradle.commons.utils.DefaultPathVisitor;

import org.gradle.api.GradleException;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyBuildsTask extends AbstractTask {

	public CopyBuildsTask() {
		setDescription("Copy the APKs/App Bundles on the build directory to the specified target directory");
	}

	@Override
	protected void onExecute() throws IOException {

		String targetBuildsDirPath = propertyResolver.getStringProp("TARGET_BUILDS_DIR_PATH");
		if (targetBuildsDirPath == null) {
			throw new GradleException("Missing TARGET_BUILDS_DIR_PATH parameter");
		}


		Files.walkFileTree(Paths.get(getProject().getBuildDir() + "/build/outputs/bundle"), new DefaultPathVisitor() {

			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
				if (path.endsWith(".aab") && !path.endsWith("unaligned.aab")) {
					try {
						Files.copy(path, Paths.get(targetBuildsDirPath + path.getFileName()));
						log("Copied App Bundle from " + path + " to " + targetBuildsDirPath);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				return FileVisitResult.CONTINUE;
			}
		});

		Files.walkFileTree(Paths.get(getProject().getBuildDir() + "/build/outputs/apk"), new DefaultPathVisitor() {

			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
				if (path.endsWith(".apk") && !path.endsWith("unaligned.apk")) {
					try {
						Files.copy(path, Paths.get(targetBuildsDirPath + path.getFileName()));
						log("Copied APK from " + path + " to " + targetBuildsDirPath);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}

}
