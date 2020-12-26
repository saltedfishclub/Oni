package io.ib67.oni;

import io.ib67.oni.maven.Result;
import io.ib67.oni.maven.config.Dependency;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MavenDownloader {
    public static final List<URI> DEFAULT_REPOSITORIES;
    private final List<URI> repositories;
    static {
            DEFAULT_REPOSITORIES = Arrays.asList(
                    URI.create("https://repo.sfclub.cc/snapshots"),
                    URI.create("https://repo.sfclub.cc/jitpack-proxy"),
                    URI.create("https://jitpack.io/"),
                    URI.create("https://maven.aliyun.com/nexus/content/groups/public/"),
                    URI.create("https://repo.maven.apache.org/maven2/")
            );
    }

    public MavenDownloader(List<URI> extraRepositories) {
        repositories = DEFAULT_REPOSITORIES;
        if (extraRepositories != null) repositories.addAll(extraRepositories);
    }

    public static File dependencyToFile(Dependency depend) {
        return new File("./lib/" + depend.artifactId + "-" + depend.version + ".jar");
    }

    public Result downloadArtifact(Dependency depend) {
        File targetDir = dependencyToFile(depend);
        targetDir.getParentFile().mkdirs();
        return downloadArtifact(depend, targetDir);
    }

    public List<Result> downloadAll(List<Dependency> deps) {
        List<Result> results = new LinkedList<>();
        deps.forEach(d -> results.add(downloadArtifact(d)));
        return results;
    }

    public Result downloadArtifact(Dependency depend, File file) {
        System.out.println("Downloading " + depend.artifactId + " v" + depend.version);
        boolean succeed = false;
        if (depend.boundedRepositories != null) {
            try {
                FileUtils.copyURLToFile(URI.create(depend.boundedRepositories + depend.asArtifactUrlPart()).toURL(), file);
                return Result.builder().dependency(depend).downloadResult(file).isSucceed(true).build();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to download " + depend.boundedRepositories + depend.asArtifactUrlPart());
                return Result.builder().dependency(depend).downloadResult(file).isSucceed(false).build();
            }
        }
        for(URI uri:repositories) {
            try {
                succeed=true;
                FileUtils.copyURLToFile(new URL(uri.toString()+depend.asArtifactUrlPart()), file);
            } catch (IOException ignored) {
                succeed=false;
            }
        }
        return Result.builder().downloadResult(file).isSucceed(succeed).dependency(depend).build();
    }
}
