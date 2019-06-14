package com.jdroid.gradle.android;

public class AndroidDynamicFeatureGradlePlugin extends AndroidLibraryGradlePlugin {

	@Override
	protected void applyAndroidPlugin() {
		applyPlugin("com.android.dynamic-feature");
	}

	@Override
	protected boolean fetchIsPublicationConfigurationEnabled() {
		return false;
	}

	@Override
	protected Class<? extends AndroidLibraryGradlePluginExtension> getExtensionClass() {
		return AndroidDynamicFeatureGradlePluginExtension.class;
	}
}

