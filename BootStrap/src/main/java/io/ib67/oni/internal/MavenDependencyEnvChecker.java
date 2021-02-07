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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class MavenDependencyEnvChecker implements IEnvChecker {
    private ConfigurableMavenResolverSystem downloader;
    private Logger logger;
    private OniSetting oniSetting;
    private BootstrappedPlugin module;
    private static final String MAVEN_RESOLVER_MD5 = "bca3b49fe5eab6b59e78d844d58a89a1";
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
        if (oniSetting.verbose) {
            logger.info("[Verbose] Initilizing Downloader");
        }
        if (!initializeDownloader(false)) {
            ctx.setState(LaunchState.FAIL);
            logger.warning("Failed to download/verify Resolver,Please check Oni Document for help.");
            return;
        }
        if (oniSetting.verbose) {
            logger.info("[Verbose] Starting injection");
        }
        if (!startInjection(true)) {
            logger.warning("Failed to load dependencies,plugin will not work.");
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

    @Override
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
            int counter = 0;
            for (String r : oniSetting.additionalRepos) {
                downloader.withRemoteRepo(String.valueOf(++counter), r, "default");
            }
            Logger.getLogger(LogTransferListener.class.getName()).setFilter(msg -> !msg.getMessage().contains("Failed downloading") && !msg.getMessage().contains("not found"));
            return true; // Already loaded
        } catch (ClassNotFoundException ignored) {
            //oof
        }
        if (tried) {
            return false;
        }
        File file = new File("./libs/MavenResolver.jar");

        if (file.exists() && getFileMD5(file).toLowerCase().equals(MAVEN_RESOLVER_MD5)) {
            Loader.addPath(file, Bukkit.class.getClassLoader());
            return initializeDownloader(true);
        }
        for (String u : MAVEN_RESOLVER_PROVIDERS) {
            try {
                File f = downloadUsingNIO(u, "./libs/MavenResolver.jar");
                if (getFileMD5(f).toLowerCase().equals(MAVEN_RESOLVER_MD5)) {
                    Loader.addPath(file, Bukkit.class.getClassLoader());
                    return initializeDownloader(true);
                }
            } catch (IOException e) {
                if (oniSetting.verbose) e.printStackTrace();
                logger.warning("Failed to download downloader when using provider " + u);
                logger.warning("Trying next..");
                if (new File("./libs/MavenResolver.jar").exists()) {
                    try {
                        Files.delete(Paths.get("./libs/MavenResolver.jar"));
                    } catch (Throwable ignored) {
                    }
                }
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

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
