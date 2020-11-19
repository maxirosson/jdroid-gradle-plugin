package com.jdroid.gradle.commons;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.publish.maven.MavenPom;
import org.gradle.api.publish.maven.MavenPomDeveloper;
import org.gradle.api.publish.maven.MavenPomDeveloperSpec;
import org.gradle.api.publish.maven.MavenPomIssueManagement;
import org.gradle.api.publish.maven.MavenPomLicense;
import org.gradle.api.publish.maven.MavenPomLicenseSpec;
import org.gradle.api.publish.maven.MavenPomOrganization;
import org.gradle.api.publish.maven.MavenPomScm;

public class JdroidPom {

	public Action<? super MavenPom> createMavenPom(Project project, BaseGradleExtension jdroid, String artifactId, String artifactPackaging) {
		Action<MavenPom> mavenPom = jdroid.getPublishingPom();
		if (mavenPom == null) {
			mavenPom = new Action<MavenPom>() {
				@Override
				public void execute(MavenPom mavenPom) {
					mavenPom.getName().set(artifactId);
					mavenPom.getDescription().set(project.getDescription() != null ? project.getDescription() : project.getRootProject().getDescription());

					// TODO Not working
					mavenPom.setPackaging(artifactPackaging);

					mavenPom.getUrl().set(jdroid.propertyResolver.getStringProp("POM_URL"));
					mavenPom.getInceptionYear().set(jdroid.propertyResolver.getStringProp("POM_INCEPTION_YEAR"));
					mavenPom.organization(new Action<MavenPomOrganization>() {
						@Override
						public void execute(MavenPomOrganization mavenPomOrganization) {
							mavenPomOrganization.getName().set(jdroid.propertyResolver.getStringProp("POM_ORGANIZATION_NAME"));
							mavenPomOrganization.getUrl().set(jdroid.propertyResolver.getStringProp("POM_ORGANIZATION_URL"));
						}

					});
					mavenPom.licenses(new Action<MavenPomLicenseSpec>() {
						@Override
						public void execute(MavenPomLicenseSpec mavenPomLicenseSpec) {
							mavenPomLicenseSpec.license(new Action<MavenPomLicense>() {
								@Override
								public void execute(MavenPomLicense mavenPomLicense) {
									mavenPomLicense.getName().set(jdroid.propertyResolver.getStringProp("POM_LICENSE_NAME"));
									mavenPomLicense.getUrl().set(jdroid.propertyResolver.getStringProp("POM_LICENSE_URL"));
									mavenPomLicense.getDistribution().set("repo");
								}

							});
						}

					});
					mavenPom.developers(new Action<MavenPomDeveloperSpec>() {
						@Override
						public void execute(MavenPomDeveloperSpec mavenPomDeveloperSpec) {
							mavenPomDeveloperSpec.developer(new Action<MavenPomDeveloper>() {
								@Override
								public void execute(MavenPomDeveloper mavenPomDeveloper) {
									mavenPomDeveloper.getName().set(jdroid.propertyResolver.getStringProp("POM_DEVELOPER_NAME"));
									mavenPomDeveloper.getEmail().set(jdroid.propertyResolver.getStringProp("POM_DEVELOPER_EMAIL"));
								}

							});
						}

					});
					mavenPom.scm(new Action<MavenPomScm>() {
						@Override
						public void execute(MavenPomScm mavenPomScm) {
							mavenPomScm.getConnection().set("scm:git:" + jdroid.getRepositorySshUrl());
							mavenPomScm.getDeveloperConnection().set("scm:git:" + jdroid.getRepositorySshUrl());
							mavenPomScm.getUrl().set(jdroid.getRepositorySshUrl());
						}

					});
					mavenPom.issueManagement(new Action<MavenPomIssueManagement>() {
						@Override
						public void execute(MavenPomIssueManagement mavenPomIssueManagement) {
							mavenPomIssueManagement.getSystem().set("GitHub");
							mavenPomIssueManagement.getUrl().set(jdroid.getRepositoryUrl() + "/issues");
						}

					});
					configure(project, mavenPom);
				}

			};
		}
		return mavenPom;
	}

	protected void configure(Project project, MavenPom mavenPom) {
		// Do nothing
	}

}
