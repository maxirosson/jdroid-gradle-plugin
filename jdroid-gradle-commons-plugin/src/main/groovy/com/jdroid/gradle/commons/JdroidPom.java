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

// TODO Allow to configure these properties
public class JdroidPom {

	public Action<? super MavenPom> createMavenPom(Project project, PropertyResolver propertyResolver, String artifactId, String artifactPackaging) {
		return new Action<MavenPom>() {
			@Override
			public void execute(MavenPom mavenPom) {
				mavenPom.getName().set(artifactId);
				mavenPom.getDescription().set(project.getDescription() != null ? project.getDescription() : project.getRootProject().getDescription());

				// TODO Not working
				mavenPom.setPackaging(artifactPackaging);

				mavenPom.getUrl().set("https://jdroidtools.com");
				mavenPom.getInceptionYear().set("2011");
				mavenPom.organization(new Action<MavenPomOrganization>() {
					@Override
					public void execute(MavenPomOrganization mavenPomOrganization) {
						mavenPomOrganization.getName().set("Jdroid");
						mavenPomOrganization.getUrl().set("https://jdroidtools.com");
					}

				});
				mavenPom.licenses(new Action<MavenPomLicenseSpec>() {
					@Override
					public void execute(MavenPomLicenseSpec mavenPomLicenseSpec) {
						mavenPomLicenseSpec.license(new Action<MavenPomLicense>() {
							@Override
							public void execute(MavenPomLicense mavenPomLicense) {
								mavenPomLicense.getName().set("The Apache License, Version 2.0");
								mavenPomLicense.getUrl().set("http://www.apache.org/licenses/LICENSE-2.0.txt");
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
								mavenPomDeveloper.getName().set("Maxi Rosson");
								mavenPomDeveloper.getEmail().set("contact@jdroidtools.com");
							}

						});
					}

				});
				mavenPom.scm(new Action<MavenPomScm>() {
					@Override
					public void execute(MavenPomScm mavenPomScm) {
						mavenPomScm.getConnection().set("scm:git:" + getRepositorySshUrl(propertyResolver));
						mavenPomScm.getDeveloperConnection().set("scm:git:" + getRepositorySshUrl(propertyResolver));
						mavenPomScm.getUrl().set(getRepositorySshUrl(propertyResolver));
					}

				});
				mavenPom.issueManagement(new Action<MavenPomIssueManagement>() {
					@Override
					public void execute(MavenPomIssueManagement mavenPomIssueManagement) {
						mavenPomIssueManagement.getSystem().set("GitHub");
						mavenPomIssueManagement.getUrl().set(getRepositoryUrl(propertyResolver) + "/issues");
					}

				});
				configure(project, mavenPom);
			}

		};
	}

	private String getRepositorySshUrl(PropertyResolver propertyResolver) {
		return "git@github.com:" + getRepositoryOwner(propertyResolver) + "/" + getRepositoryName(propertyResolver) + ".git";
	}

	private String getRepositoryUrl(PropertyResolver propertyResolver) {
		return "https://github.com/" + getRepositoryOwner(propertyResolver) + "/" + getRepositoryName(propertyResolver);
	}

	private String getRepositoryOwner(PropertyResolver propertyResolver) {
		return propertyResolver.getStringProp("JDROID_GITHUB_REPOSITORY_OWNER", "maxirosson");
	}

	private String getRepositoryName(PropertyResolver propertyResolver) {
		return propertyResolver.getStringProp("JDROID_GITHUB_REPOSITORY_NAME");
	}

	protected void configure(Project project, MavenPom mavenPom) {
		// Do nothing
	}

}
