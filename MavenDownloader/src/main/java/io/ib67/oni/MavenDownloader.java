package io.ib67.oni;

import io.ib67.oni.maven.config.Dependency;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MavenDownloader {
    public static final List<URI> DEFAULT_REPOSITORIES;
    private final List<URI> repositories;
    static {
            DEFAULT_REPOSITORIES = Arrays.asList(
                    URI.create("https://maven.hbxueli.com/repository/maven-public/"),
                    URI.create("https://jitpack.io/"),
                    URI.create("https://maven.aliyun.com/nexus/content/groups/public/"),
                    URI.create("https://repo.maven.apache.org/maven2/")
            );
    }
    public MavenDownloader(List<URI> extraRepositories){
        repositories=DEFAULT_REPOSITORIES;
        if(extraRepositories!=null) repositories.addAll(extraRepositories);
    }
    public boolean downloadArtifact(Dependency depend){
        File targetDir=new File("./lib/"+depend.artifactId+"-"+depend.version+".jar");
        targetDir.getParentFile().mkdirs();
        return downloadArtifact(depend,targetDir);
    }
    public boolean downloadArtifact(Dependency depend,File file){
        boolean succeed=false;
        if(depend.boundedRepositories!=null){
            try{
                FileUtils.copyURLToFile(URI.create(depend.boundedRepositories+depend.asArtifactUrlPart()).toURL(),file);
                return true;
            }catch(IOException e){
                e.printStackTrace();
                System.err.println("Failed to download " + depend.boundedRepositories + depend.asArtifactUrlPart());
                return false;
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
        return succeed;
    }
}
