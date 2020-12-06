package io.ib67.oni;

import io.ib67.oni.util.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class OniLoader extends JavaPlugin {
    public void onEnable() {
        new Metrics(this, 9563);
    }
}
