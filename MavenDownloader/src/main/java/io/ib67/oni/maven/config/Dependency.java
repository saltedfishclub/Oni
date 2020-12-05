package io.ib67.oni.maven.config;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Dependency {
    public final String artifactId;
    public final String groupId;
    public final String version;
    public String boundedRepositories;
    public String asArtifactUrlPart(){
        return groupId+"/"+artifactId+"/"+version+"/"+artifactId+"-"+version+".jar";
    }
}
