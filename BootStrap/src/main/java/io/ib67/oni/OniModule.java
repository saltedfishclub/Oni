package io.ib67.oni;

import com.google.gson.Gson;
import io.ib67.oni.internal.Loader;
import io.ib67.oni.internal.OniSetting;
import io.ib67.oni.maven.Result;
import io.ib67.oni.maven.config.Dependency;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

/**
 * Oni plugin bootstrap entrance.
 */
public abstract class OniModule extends JavaPlugin {
    private static final Gson gson = new Gson();
    private Oni oniCore;
    private MavenDownloader downloader;
    private OniSetting oniSetting;
    private List<Dependency> failedDeps = new LinkedList<>();

    @Override
    public final void onLoad() {
        beforeEnable();
    }

    @Override
    public final void onDisable() {
        onStop();
    }

    protected final Oni getOni() {
        Validate.notNull(oniCore, "Oni is not loaded yet!");
        return oniCore;
    }

    @Override
    public final void onEnable() {
        Reader textRes = getTextResource("oni.setting.json");
        Validate.notNull(textRes, "Broken plugin (Missing oni.setting.json)");
        oniSetting = gson.fromJson(textRes, OniSetting.class);
        downloader = new MavenDownloader(oniSetting.additionalRepos);
/*        try {
            Class.forName("io.ib67.oni.Oni");
            // Class found!
            String[] versions = oniSetting.oniVersion.split("\\.");
            if (versions.length < 2) {
                getLogger().warning("Illegal oni version,Please contact plugin author.");
                return;
            }
            if (Integer.parseInt(versions[0]) > Oni.VERSION) {
                //Oni out of date.
                if (!startInjection(true)) {
                    getLogger().warning("Failed to load oni,plugin will not work.");
                    setEnabled(false);
                    return;
                }
            }
        } catch (ClassNotFoundException ignored) {*/
            // We're first!
        if (!startInjection(true)) {
            getLogger().warning("Failed to load oni,plugin will not work.");
            setEnabled(false);
            return;
        }
        //}
        //Resolve dependencies.
        if (!resolveDependencies(oniSetting.dependencies)) {
            getLogger().warning("Failed to solve dependencies,plugin will not work.");
            getLogger().warning("Unable to resolve:");
            failedDeps.forEach(d -> getLogger().warning(d.groupId + ":" + d.artifactId + ":" + d.version));
            setEnabled(false);
            return;
        }
        onStart();
    }

    /**
     * DO you really know what are you doing?
     */
    public void beforeEnable() {
    }

    public abstract void onStart();

    public abstract void onStop();

    private boolean resolveDependencies(List<Dependency> deps) {
        List<Result> results = downloader.downloadAll(deps);
        results.stream().filter(e -> !e.isSucceed).forEachOrdered(e -> failedDeps.add(e.dependency));
        boolean succeed = results.stream().anyMatch(e -> !e.isSucceed && !e.dependency.optional);
        if (!succeed) return false;
        results.stream().filter(e -> e.isSucceed).forEachOrdered(e -> {
            Loader.addPath(e.downloadResult, this.getClassLoader());
        });
        return true;
    }

    private boolean startInjection(boolean capabilityMode) {
        Dependency dep = new Dependency("Oni-all", "io.ib67.oni", oniSetting.oniVersion);
        File file = MavenDownloader.dependencyToFile(dep);
        if (!file.exists() || file.getTotalSpace() == 0) {
            // Download oni first
            Result result = downloader.downloadArtifact(dep);
            if (!result.isSucceed) {
                getLogger().warning("Failed to download Oni! Depended Oni version: " + dep.version);
                getLogger().warning("Plugin will not work. Check for manual for help.");
                return false;
            }
        }
        // download succeed or exists
        ClassLoader cl = Bukkit.class.getClassLoader();
        if (capabilityMode) {
            cl = this.getClassLoader();
        }
        Loader.addPath(file, cl);
        if (Loader.forName("io.ib67.oni.Oni", true, cl) == null) {
            return false;
        }
        this.oniCore = Oni.of(this);
        return true;
    }

}
