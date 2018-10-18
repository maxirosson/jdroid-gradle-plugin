package com.jdroid.gradle.android.task;

import com.jdroid.gradle.commons.tasks.AbstractTask;
import com.jdroid.gradle.commons.utils.DefaultPathVisitor;

import org.gradle.api.GradleException;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

		Files.walkFileTree(Paths.get(getProject().getBuildDir() + "/outputs/bundle"), new DefaultPathVisitor() {

			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
				if (path.getFileName().toString().endsWith(".aab") && !path.getFileName().toString().endsWith("unaligned.aab")) {
					try {
						String newFileName = path.getFileName().toString().replace(".aab", "-v" + getProject().getVersion() + ".aab");
						String appName = propertyResolver.getStringProp("APP_BUILD_BASE_NAME", getProject().getProjectDir().getParentFile().getName());
						newFileName = newFileName.replace("app", appName + "-" + path.getParent().getFileName().toString());
						String target = targetBuildsDirPath + path.getParent().getFileName().toString() + "/" + newFileName;
						Files.copy(path, Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
						log("Copied App Bundle from " + path + " to " + target);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				return FileVisitResult.CONTINUE;
			}
		});

		Files.walkFileTree(Paths.get(getProject().getBuildDir() + "/outputs/apk"), new DefaultPathVisitor() {

			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
				if (path.getFileName().toString().endsWith(".apk") && !path.getFileName().toString().endsWith("unaligned.apk")) {
					try {
						String target = targetBuildsDirPath + path.getParent().getFileName().toString() + "/" + path.getFileName();
						Files.copy(path, Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
						log("Copied APK from " + path + " to " + target);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}

}
