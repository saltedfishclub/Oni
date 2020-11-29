package io.ib67.oni.maven.config;

import eu.mikroskeem.picomaven.Dependency;

import java.net.URI;
import java.util.List;

public class OniSetting {
    public String pluginMainClass;
    public String oniVersion;
    public List<Dependency> dependencies;
    public List<URI> repositories;
}
