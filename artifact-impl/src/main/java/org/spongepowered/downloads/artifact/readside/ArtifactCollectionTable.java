package org.spongepowered.downloads.artifact.readside;

@Table
public final class ArtifactCollectionTable {

    private String version;
    private String artifactId;
    private String mavenCoordinates;
    private String groupCoordinates;
    private String groupName;
    private String groupWebsite;
    private boolean broken;
    private String variant;
    private String downloadUrl;
    private String md5;
    private String sha1;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getMavenCoordinates() {
        return mavenCoordinates;
    }

    public void setMavenCoordinates(String mavenCoordinates) {
        this.mavenCoordinates = mavenCoordinates;
    }

    public String getGroupCoordinates() {
        return groupCoordinates;
    }

    public void setGroupCoordinates(String groupCoordinates) {
        this.groupCoordinates = groupCoordinates;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupWebsite() {
        return groupWebsite;
    }

    public void setGroupWebsite(String groupWebsite) {
        this.groupWebsite = groupWebsite;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }
}
