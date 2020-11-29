package io.ib67.oni;

import com.google.gson.Gson;
import io.ib67.oni.maven.config.OniSetting;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Reader;
import java.nio.file.Path;

/**
 * Oni plugin bootstrap entrance.
 */
public final class OniBootstrap extends JavaPlugin {
    private static final Gson gson=new Gson();
    private Oni oniCore;
    @Override
    public void onEnable(){
        // resolve dependencies
        Reader settingReader=getTextResource("oni.setting.json");
        if(settingReader==null){
            Bukkit.getServer().getScheduler().runTask(this,()->{
                getLogger().warning("CANT LOAD ONI-SETTING!");
                getLogger().warning("* IF YOU ARE END-USER,PLEASE FEEDBACK THIS MESSAGE TO DEVELOPER.");
                getLogger().warning("* IF YOU ARE DEVELOPER,PLEASE CHECK GRADLE BUILD SETTINGS");
                getLogger().warning("PLUGIN IS DISABLING");
                this.setEnabled(false);
            });
            return;
        }
        OniSetting oniSetting=gson.fromJson(settingReader,OniSetting.class);
        try {
            Class.forName("io.ib67.oni.Oni");
        } catch (ClassNotFoundException e) {
            getLogger().info("OniCore Not Found,Downloading...");
            try{
                Path path=MavenDownloader.downloadOni(oniSetting.oniVersion);
                try {
                    Plugin plugin = Bukkit.getPluginManager().loadPlugin(path.toFile());
                    plugin.onLoad();
                    Bukkit.getPluginManager().enablePlugin(plugin);
                } catch (Throwable t) {
                    Bukkit.getServer().getScheduler().runTask(this,()->{
                        getLogger().warning("Cannot load OniCore!Please download OniCore manually.");
                        this.setEnabled(false);
                    });
                    return;
                }
            }catch(InterruptedException d){
                    d.printStackTrace();
                    getLogger().warning("Download task was interrupted!Please download OniCore manually.");
                    this.setEnabled(false);
                return;
            }
        }
        //Oni load successfully
        oniCore=Oni.of(this);
        oniCore.getDependencyManager().download(oniSetting,c->{});
    }
}
