package io.ib67.oni.util.network;

import io.ib67.oni.MavenDownloader;
import io.ib67.oni.Oni;
import io.ib67.oni.maven.config.OniSetting;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class DependencyManager {
    private final MavenDownloader mavenDownloader=new MavenDownloader();
    private final Oni oni;
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static MethodHandle addUrl;
    static{
        try{
            Method m=URLClassLoader.class.getMethod("addURL", URL.class);
            m.setAccessible(true);
            addUrl=lookup.unreflect(m);
        }catch(Throwable ignored){}
    }
    public DependencyManager(Oni oni){
        this.oni=oni;
    }
    public void download(OniSetting setting, Consumer<List<Path>> callback){
        mavenDownloader.download(setting,new File("libs"),paths -> {
            paths.forEach(this::loadIntoBukkit);
            if(setting.dependencies.size()!=paths.size()){
                Oni.getGlobalLogger().warning("Dependencies maybe something wrong.");
            }
            callback.accept(paths);
        });
    }
    @SneakyThrows
    private void loadIntoBukkit(Path file){
        addUrl.bindTo(oni.getPlugin().getClass().getClassLoader()).invokeWithArguments(file.toUri().toURL());
    }
}
