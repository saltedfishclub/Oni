package io.ib67.oni;

import com.google.gson.Gson;
import io.ib67.oni.config.OniSetting;
import io.ib67.oni.maven.config.Dependency;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * Oni plugin bootstrap entrance.
 */
public abstract class OniModule extends JavaPlugin {
    private static final Gson gson = new Gson();
    private Oni oniCore;
    private MavenDownloader downloader;

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
        // resolve dependencies
        Reader settingReader = getTextResource("oni.setting.json");
        if (settingReader == null) {
                getLogger().warning("CANT LOAD ONI-SETTING!");
                getLogger().warning("* IF YOU ARE END-USER,PLEASE FEEDBACK THIS MESSAGE TO DEVELOPER.");
                getLogger().warning("* IF YOU ARE DEVELOPER,PLEASE CHECK GRADLE BUILD SETTINGS");
                getLogger().warning("PLUGIN IS DISABLING");
                this.setEnabled(false);
                return;
        }
        OniSetting oniSetting = gson.fromJson(settingReader, OniSetting.class);
        downloader=new MavenDownloader(oniSetting.additionalRepos);
        try {
            settingReader.close();
        } catch (IOException ignored) {

        }
        if (!loadOni(false, oniSetting)) {
            this.setEnabled(true);
            getLogger().warning("Failed to load OniCore,Plugin shutting down.");
            return;
        }
        //Resolve other deps
        for (Dependency e : oniSetting.dependencies) {
            File targetDir = new File("./lib/" + e.artifactId + "-" + e.version + ".jar");
            getLogger().info("Resolving Dependency: " + e.artifactId + "-" + e.version);
            if (!downloader.downloadArtifact(e, targetDir)) {
                getLogger().info("FAILED TO DOWNLOAD!");
                this.setEnabled(false);
                return;
            }
            try {
                Oni.loadIntoClassloader(Oni.class.getClassLoader(), targetDir.toURI().toURL());
            } catch (Throwable ignored) {
            }
        }
        getLogger().info("Plugin loaded.");
    }

    /**
     * Don't use it or you really know what are you doing.
     */
    @Deprecated
    public abstract void beforeEnable();

    public abstract void onStart();

    public abstract void onStop();

    @SuppressWarnings("unchecked")
    private boolean loadOni(boolean tried,OniSetting oniSetting) {
        getLogger().info("Trying to load Oni..");
        try{
            Class<Oni> clazz=(Class<Oni>) Class.forName("io.ib67.Oni");
            String[] versions=oniSetting.oniVersion.split("-");
            if(versions.length<2){
                getLogger().warning("Illegal version format!quitting...");
                return false;
            }
            if (Integer.valueOf(versions[0])>Oni.VERSION) {
                getLogger().warning("The current oni is old!Please update oni. (find the latest oni in ./lib/)");
                return false;
            }
            oniCore=Oni.of(this);
            return true;
        }catch(ClassNotFoundException e){
            File target=new File("./lib/Oni-" + oniSetting.oniVersion + ".jar");
            if(!downloadOni(oniSetting,target)){
                getLogger().warning("FAILED.");
                return false;
            }
            try {
                Bukkit.getPluginManager().loadPlugin(target);
            }catch(Exception t){
                t.printStackTrace();
                return false;
            }
            if(!tried){
                loadOni(true,oniSetting);
            }
        }
        return false;
    }
    private boolean downloadOni(OniSetting oniSetting,File file){
        Dependency dep=new Dependency("Oni","io.ib67.oni", oniSetting.oniVersion);
        return downloader.downloadArtifact(dep,file);
    }

}
