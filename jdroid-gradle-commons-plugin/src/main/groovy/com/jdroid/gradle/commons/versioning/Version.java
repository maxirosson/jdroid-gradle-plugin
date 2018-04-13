package com.jdroid.gradle.commons.versioning;

import com.jdroid.gradle.commons.PropertyResolver;
import com.jdroid.gradle.commons.utils.ProjectUtils;
import com.jdroid.gradle.commons.utils.TypeUtils;
import com.jdroid.java.date.DateUtils;

import org.gradle.api.Project;

public class Version {
	
	private Project project;
	private Integer versionMajor;
	private Integer versionMinor;
	private Integer versionPatch;
	private String versionClassifier;
	private Boolean isVersionTimestampEnabled;
	private Boolean isSnapshot;
	private String featureName;
	private String featureBranchPrefix;
	private Boolean isLocal;
	
	private Integer maximumVersion;
	
	public Version(Project project, final String version) {
		
		this.project = project;
		PropertyResolver propertyResolver = new PropertyResolver(project);
		
		maximumVersion = propertyResolver.getIntegerProp("MAXIMUM_VERSION", getDefaultMaximumVersion());
		
		String[] versionSplit = version.split("\\.");
		if (versionSplit.length != 3) {
			throw new RuntimeException("The version [" + version + "] is not a valid Semantic Versioning");
		}
		
		versionMajor = TypeUtils.getInteger(versionSplit[0]);
		if (versionMajor > maximumVersion || versionMajor < 0) {
			throw new RuntimeException("The version major [" + String.valueOf(versionMajor) + "] should be a number between 0 and " + maximumVersion);
		}
		
		versionMinor = TypeUtils.getInteger(versionSplit[1]);
		if (versionMinor > maximumVersion || versionMinor < 0) {
			throw new RuntimeException("The version minor [" + String.valueOf(versionMinor) + "] should be a number between 0 and " + maximumVersion);
		}
		
		versionPatch = TypeUtils.getInteger(versionSplit[2]);
		if (versionPatch > maximumVersion || versionPatch < 0) {
			throw new RuntimeException("The version patch [" + String.valueOf(versionPatch) + "] should be a number between 0 and " + maximumVersion);
		}
		
		isSnapshot = propertyResolver.getBooleanProp("SNAPSHOT", true);
		isVersionTimestampEnabled = propertyResolver.getBooleanProp("VERSION_TIMESTAMP_ENABLED", false);
		isLocal = propertyResolver.getBooleanProp("LOCAL", false);
		featureBranchPrefix = propertyResolver.getStringProp("FEATURE_BRANCH_PREFIX", "feature/");
		
		versionClassifier = propertyResolver.getStringProp("VERSION_CLASSIFIER");
		if (versionClassifier == null) {
			
			String gitBranch = ProjectUtils.getJdroidExtension(project).getGitBranch();
			Boolean isFeatureBranch = gitBranch != null && gitBranch.startsWith(featureBranchPrefix);
			if (isFeatureBranch) {
				featureName = gitBranch.replace(featureBranchPrefix, "");
				versionClassifier = featureName;
			}
			
			if (isLocal) {
				if (versionClassifier == null) {
					versionClassifier = "";
				} else {
					versionClassifier += "-";
				}
				
				versionClassifier += "LOCAL";
			}
			
			if (isVersionTimestampEnabled) {
				if (versionClassifier == null) {
					versionClassifier = "";
				} else {
					versionClassifier += "-";
				}
				
				versionClassifier += DateUtils.format(DateUtils.now(), "YYYYMMddHHmmss");
			}
			
			if (isSnapshot) {
				if (versionClassifier == null) {
					versionClassifier = "";
				} else {
					versionClassifier += "-";
				}
				
				versionClassifier += "SNAPSHOT";
			}
			
		}
		
	}
	
	public void incrementMajor() {
		if (versionMajor < maximumVersion) {
			versionMajor += 1;
			versionMinor = 0;
			versionPatch = 0;
		} else {
			throw new RuntimeException("The version major [" + String.valueOf(versionMajor) + "] can't be incremented. Maximum value achieved.");
		}
	}
	
	public void incrementMinor() {
		if (versionMinor < maximumVersion) {
			versionMinor += 1;
			versionPatch = 0;
		} else {
			incrementMajor();
		}
	}
	
	public void incrementPatch() {
		if (versionPatch < maximumVersion) {
			versionPatch += 1;
		} else {
			incrementMinor();
		}
	}
	
	protected Integer getDefaultMaximumVersion() {
		return 999;
	}
	
	public String getBaseVersion() {
		return String.valueOf(versionMajor) + "." + String.valueOf(versionMinor) + "." + String.valueOf(versionPatch);
	}
	
	public String toString() {
		String versionName = getBaseVersion();
		if (versionClassifier != null && !versionClassifier.isEmpty()) {
			versionName += "-" + versionClassifier;
		}
		
		return versionName;
	}
	
	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}
	
	public Integer getVersionMajor() {
		return versionMajor;
	}
	
	public void setVersionMajor(Integer versionMajor) {
		this.versionMajor = versionMajor;
	}
	
	public Integer getVersionMinor() {
		return versionMinor;
	}
	
	public void setVersionMinor(Integer versionMinor) {
		this.versionMinor = versionMinor;
	}
	
	public Integer getVersionPatch() {
		return versionPatch;
	}
	
	public void setVersionPatch(Integer versionPatch) {
		this.versionPatch = versionPatch;
	}
	
	public String getVersionClassifier() {
		return versionClassifier;
	}
	
	public void setVersionClassifier(String versionClassifier) {
		this.versionClassifier = versionClassifier;
	}
	
	public Boolean getIsVersionTimestampEnabled() {
		return isVersionTimestampEnabled;
	}
	
	public void setIsVersionTimestampEnabled(Boolean isVersionTimestampEnabled) {
		this.isVersionTimestampEnabled = isVersionTimestampEnabled;
	}
	
	public Boolean getIsSnapshot() {
		return isSnapshot;
	}
	
	public void setIsSnapshot(Boolean isSnapshot) {
		this.isSnapshot = isSnapshot;
	}
	
	public String getFeatureName() {
		return featureName;
	}
	
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	
	public String getFeatureBranchPrefix() {
		return featureBranchPrefix;
	}
	
	public void setFeatureBranchPrefix(String featureBranchPrefix) {
		this.featureBranchPrefix = featureBranchPrefix;
	}
	
	public Boolean getIsLocal() {
		return isLocal;
	}
	
	public void setIsLocal(Boolean isLocal) {
		this.isLocal = isLocal;
	}
}
