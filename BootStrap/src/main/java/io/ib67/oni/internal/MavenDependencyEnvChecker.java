package io.ib67.oni.internal;

import io.ib67.oni.BootstrappedPlugin;
import io.ib67.oni.bootstrap.IEnvChecker;
import io.ib67.oni.bootstrap.LaunchStage;
import io.ib67.oni.bootstrap.LaunchState;
import io.ib67.oni.bootstrap.PreparingContext;
import io.ib67.oni.internal.data.Dependency;
import org.bukkit.Bukkit;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.impl.maven.logging.LogTransferListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class MavenDependencyEnvChecker implements IEnvChecker {
    private ConfigurableMavenResolverSystem downloader;
    private Logger logger;
    private OniSetting oniSetting;
    private BootstrappedPlugin module;
    private static final List<String> DEFAULT_REPOSITORIES = Arrays.asList(
            "https://repo.sfclub.cc/releases/",
            "https://repo.sfclub.cc/snapshots/",
            "https://maven.aliyun.com/nexus/content/groups/public/",
            "https://jitpack.io/",
            "https://repo.maven.apache.org/maven2/"
    ); //todo use global config.
    private static final List<String> MAVEN_RESOLVER_PROVIDERS = Arrays.asList(
            "https://storage.sfclub.cc/resolver.jar",
            "https://pro-video.xiaoheiban.cn/shi/aa8a21f2-2547-43c2-a92f-6845e4b2f16f.jar"
            //If you're willing to support us..
            //E-Mail: icebear67@sfclub.cc
    );

    @Override
    public void apply(PreparingContext ctx) {
        this.oniSetting = ctx.getSettings();
        this.logger = ctx.getModule().getLogger();
        this.module = ctx.getModule();
        if (!initializeDownloader(false)) {
            ctx.setState(LaunchState.FAIL);
            logger.warning("Failed to download Resolver,Please check Oni Document for help.");
            return;
        }
        if (!startInjection(true)) {
            logger.warning("Failed to load oni,plugin will not work.");
            ctx.setState(LaunchState.FAIL);
        }
    }

    private boolean resolveAndLoadDependencies(List<Dependency> deps) {
        for (Dependency dep : deps) {
            try {
                if (oniSetting.verbose) {
                    logger.info("=> Resolving: " + dep.asCoordinate());
                }
                File[] resolved = downloader.resolve(dep.asCoordinate()).withTransitivity().asFile();
                for (File file : resolved) {
                    if (oniSetting.verbose) {
                        logger.info("=> Resolved: " + file.getName() + " (" + file.getAbsolutePath() + ")");
                    }
                    ClassLoader cl = Bukkit.class.getClassLoader();
                    if (dep.compatibilityMode) {
                        cl = module.getClass().getClassLoader();
                    }
                    Loader.addPath(file, cl);
                }

            } catch (Exception e) {
                logger.warning("Failed to download/load dependency: " + dep.asCoordinate());
                if (dep.optional) {
                    logger.warning("Some function may not work well. (" + e.getMessage() + ")");
                } else {
                    logger.warning("Plugin will not work. (" + e.getMessage() + ")");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean startInjection(boolean compatibilityMode) {
        List<Dependency> depsToLoad = new ArrayList<>();
/*        if (oniSetting.oniVersion != null) {
            Dependency oni = new Dependency("io.ib67.oni", "Oni-all", oniSetting.oniVersion, "all", "jar", false);
            oni.compatibilityMode = compatibilityMode;
            depsToLoad.add(oni);
        }*/
        if (oniSetting.dependencies != null) {
            depsToLoad.addAll(oniSetting.dependencies);
        }
        if (!resolveAndLoadDependencies(depsToLoad)) {
            return false;
        }
        /*if (Loader.forName("io.ib67.oni.Oni", true, compatibilityMode ? module.getClass().getClassLoader() : Bukkit.class.getClassLoader()) == null) {
            return false;
        }*/
        // preEnable(); // Provide a space to use DI.
        return true;
    }

    public LaunchStage getLaunchStage() {
        return LaunchStage.PRE_ENABLE;
    }

    private boolean initializeDownloader(boolean tried) {
        new File("./libs").mkdirs();
        try {
            Class.forName("org.jboss.shrinkwrap.resolver.api.maven.Maven");
            if (oniSetting.additionalRepos == null) {
                oniSetting.additionalRepos = new ArrayList<>();
            }
            oniSetting.additionalRepos.addAll(DEFAULT_REPOSITORIES);
            downloader = Maven.configureResolver();
            oniSetting.additionalRepos.forEach(r -> {
                downloader.withRemoteRepo(UUID.randomUUID().toString(), r, "default");
            });
            Logger.getLogger(LogTransferListener.class.getName()).setFilter(msg -> !msg.getMessage().contains("Failed downloading") && !msg.getMessage().contains("not found"));
            return true; // Already loaded
        } catch (ClassNotFoundException ignored) {
            //oof
        }
        if (tried) {
            return false;
        }
        File file = new File("./libs/MavenResolver.jar");
        if (file.exists()) {
            Loader.addPath(file, Bukkit.class.getClassLoader());
            return initializeDownloader(true);
        }
        for (String u : MAVEN_RESOLVER_PROVIDERS) {
            try {
                Loader.addPath(downloadUsingNIO(u, "./libs/MavenResolver.jar"), Bukkit.class.getClassLoader()); // Load to global
                return initializeDownloader(true);
            } catch (IOException e) {
                if (oniSetting.verbose) e.printStackTrace();
                logger.warning("Failed to download downloader when using provider " + u);
                logger.warning("Trying next..");
            }
        }
        return false;
    }

    private File downloadUsingNIO(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
        return new File(file);
    }
}
