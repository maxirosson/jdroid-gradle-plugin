package com.jdroid.gradle.root.task;

import com.jdroid.gradle.commons.tasks.AbstractTask;
import com.jdroid.java.collections.Lists;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class CopyGitHooksTask extends AbstractTask {

	public CopyGitHooksTask() {
		setDescription("Copy Git Hooks");
	}

	@Override
	protected void onExecute() {
		List<String> hooksToCreate = Lists.newArrayList("commit-msg");
		for (String hookName : hooksToCreate) {
			File target = getProject().file(".git/hooks/" + hookName);
			InputStream source = getClass().getResourceAsStream("/git/hooks/" + hookName);
			try {
				Files.copy(source, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
