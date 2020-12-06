package io.ib67.oni.maven;

import io.ib67.oni.maven.config.Dependency;
import lombok.Builder;

import java.io.File;

@Builder
public class Result {
    public boolean isSucceed;
    public File downloadResult;
    public Dependency dependency;
}
