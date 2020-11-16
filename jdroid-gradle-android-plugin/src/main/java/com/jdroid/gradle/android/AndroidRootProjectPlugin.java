package com.jdroid.gradle.android;

import com.jdroid.gradle.root.RootProjectPlugin;

public class AndroidRootProjectPlugin extends RootProjectPlugin {

	@Override
	protected boolean fetchIsPublicationConfigurationEnabled() {
		return false;
	}
}
