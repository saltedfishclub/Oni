package io.ib67.oni.internal.data;

public class Dependency {
    public final boolean optional;
    public boolean compatibilityMode = true;
    public final String groupId;
    public final String artifactId;
    public final String version;
    public String classifier;
    public String packagingType = "jar";

    public Dependency(String groupId, String artifactId, String version, boolean optional) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.optional = optional;
    }

    public Dependency(String groupId, String artifactId, String version, String classifier, String packagingType, boolean optional) {
        //TODO Update Oni.Linker for classifier and packagingType support.
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.classifier = classifier;
        this.packagingType = packagingType;
        this.optional = optional;
    }

    public String asCoordinate() {
        if (classifier != null && packagingType != null) { //G:A:P:C:V
            return groupId + ":" + artifactId + ":" + packagingType + ":" + classifier + ":" + version;
        }
        return groupId + ":" + artifactId + ":" + version; // G:A:V
    }
}
