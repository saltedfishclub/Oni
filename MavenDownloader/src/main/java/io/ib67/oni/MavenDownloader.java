package io.ib67.oni;

import eu.mikroskeem.picomaven.Dependency;
import eu.mikroskeem.picomaven.PicoMaven;
import io.ib67.oni.maven.config.OniSetting;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MavenDownloader {
    public static final List<URI> DEFAULT_REPOSITORIES;

    static {
            DEFAULT_REPOSITORIES = Arrays.asList(
                    URI.create("https://maven.hbxueli.com/repository/maven-public/"),
                    URI.create("https://maven.aliyun.com/nexus/content/groups/public/"),
                    URI.create("https://repo.maven.apache.org/maven2"),
                    URI.create("https://jitpack.io")
            );
    }

    public void download(OniSetting setting, File destin, Consumer<List<Path>> callback){
        destin.getParentFile().mkdirs();
        destin.mkdirs();
        List<URI> repos=new LinkedList<>();
        repos.addAll(setting.repositories);
        repos.addAll(DEFAULT_REPOSITORIES);
        PicoMaven.Builder picoMavenBase = new PicoMaven.Builder()
                .withDownloadPath(destin.toPath())
                .withRepositories(repos)
                .withDependencies(setting.dependencies);
        try (PicoMaven picoMaven = picoMavenBase.build()) {
            List<Path> downloaded = picoMaven.downloadAll();
            callback.accept(downloaded);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        callback.accept(new ArrayList<>());
    }
    public static Path downloadOni(String version) throws InterruptedException {
        Path destination=Paths.get(".","lib");
        PicoMaven.Builder picoMavenBase = new PicoMaven.Builder()
                .withDownloadPath(destination)
                .withRepositories(DEFAULT_REPOSITORIES)
                .withDependencies(Arrays.asList(new Dependency("io.ib67.oni","Oni",version)));
        return picoMavenBase.build().downloadAll().get(0);
    }
}
