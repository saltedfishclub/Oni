package io.ib67.oni.config;

import io.ib67.oni.maven.config.Dependency;

import java.net.URI;
import java.util.List;

public class OniSetting {
    public String oniVersion;
    public List<Dependency> dependencies;
    public List<URI> additionalRepos;
}
