package io.ib67.oni.maven.config;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Dependency {
    public final String artifactId;
    public final String groupId;
    public final String version;
    public boolean optional = false;
    public String boundedRepositories;
    public String asArtifactUrlPart(){
        return groupId.replaceAll("\\.", "/") + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".jar";
    }
}
