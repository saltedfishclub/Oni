package io.ib67.oni;

import com.google.gson.Gson;
import io.ib67.oni.internal.Loader;
import io.ib67.oni.internal.OniSetting;
import io.ib67.oni.internal.data.Dependency;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.impl.maven.logging.LogTransferListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Oni plugin bootstrap entrance.
 */
@SuppressWarnings("unused")
public abstract class OniModule extends JavaPlugin {
    private static final Gson gson = new Gson();
    private Oni oniCore;
    private ConfigurableMavenResolverSystem downloader;
    private OniSetting oniSetting;
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
    public final void onLoad() {
        beforeEnable();
    }

    @Override
    public final void onDisable() {
        onStop();
    }

    /**
     * get Oni.
     *
     * @return Oni
     */
    protected final Oni getOni() {
        Validate.notNull(oniCore, "Oni is not loaded yet!");
        return oniCore;
    }

    @Override
    public final void onEnable() {
        Reader textRes = getTextResource("oni.setting.json");
        Validate.notNull(textRes, "Broken plugin (Missing oni.setting.json)");
        oniSetting = gson.fromJson(textRes, OniSetting.class);
        if (!initializeDownloader(false)) {
            setEnabled(false);
            getLogger().warning("Failed to download Resolver,Please check Oni Document for help.");
            return;
        }
        if (!startInjection(true)) {
            getLogger().warning("Failed to load oni,plugin will not work.");
            setEnabled(false);
            return;
        }
        onStart();
    }

    /**
     * plugin.onLoad , previous than preEnable.
     *
     * @since 1.0
     */
    public void beforeEnable() {
    }

    /**
     * Executed before oniBootstrap actually runs.
     *
     * @since 3.0
     */
    public void preEnable() {
    }

    /**
     * Executed when bootstrap finishes its work.
     *
     * @since 1.0
     */
    public abstract void onStart();

    /**
     * Executed when server shutting down.
     *
     * @since 1.0
     */
    public abstract void onStop();

    private boolean resolveAndLoadDependencies(List<Dependency> deps) {
        for (Dependency dep : deps) {
            try {
                if (oniSetting.verbose) {
                    getLogger().info("=> Resolving: " + dep.asCoordinate());
                }
                File[] resolved = downloader.resolve(dep.asCoordinate()).withTransitivity().asFile();
                for (File file : resolved) {
                    if (oniSetting.verbose) {
                        getLogger().info("=> Resolved: " + file.getName() + " (" + file.getAbsolutePath() + ")");
                    }
                    ClassLoader cl = Bukkit.class.getClassLoader();
                    if (dep.compatibilityMode) {
                        cl = this.getClassLoader();
                    }
                    Loader.addPath(file, cl);
                }

            } catch (Exception e) {
                getLogger().warning("Failed to download/load dependency: " + dep.asCoordinate());
                if (dep.optional) {
                    getLogger().warning("Some function may not work well. (" + e.getMessage() + ")");
                } else {
                    getLogger().warning("Plugin will not work. (" + e.getMessage() + ")");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean startInjection(boolean compatibilityMode) {
        Dependency oni = new Dependency("io.ib67.oni", "Oni-all", oniSetting.oniVersion, "all", "jar", false);
        oni.compatibilityMode = compatibilityMode;
        List<Dependency> depsToLoad = new ArrayList<>();
        depsToLoad.add(oni);
        if (oniSetting.dependencies != null) {
            depsToLoad.addAll(oniSetting.dependencies);
        }
        if (!resolveAndLoadDependencies(depsToLoad)) {
            return false;
        }
        if (Loader.forName("io.ib67.oni.Oni", true, compatibilityMode ? this.getClassLoader() : Bukkit.class.getClassLoader()) == null) {
            return false;
        }
        this.oniCore = Oni.of(this);
        return true;
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
                getLogger().warning("Failed to download downloader when using provider " + u);
                getLogger().warning("Trying next..");
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
