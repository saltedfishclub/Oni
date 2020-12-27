package io.ib67.oni.internal;

import io.ib67.oni.util.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A JavaPlugin for oni load as Bukkit plugin.
 *
 * @since 1.0
 */
public class OniLoader extends JavaPlugin {
    public void onEnable() {
        new Metrics(this, 9563);
    }
}
