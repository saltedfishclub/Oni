package io.ib67.oni;

import io.ib67.oni.onion.PlayerOnion;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class Oni {
    public static final int VERSION=1;
    @Getter
    private final JavaPlugin plugin;
    private static final Map<String,Oni> claimedOni=new HashMap<>();
    @Setter
    @Getter
    private String messagePrefix;
    public static final String DEFAULT_MESSAGE_PREFIX="&b%s &7>> &f";
    @Getter
    private static final List<URL> loadedDeps=new ArrayList<>();
    @Getter
    private static final Logger globalLogger=Logger.getLogger("Oni");
    private final Logger logger;
    private Oni(JavaPlugin plugin){
        this.plugin=plugin;
        logger= Logger.getLogger(plugin.getName());
        messagePrefix= String.format(DEFAULT_MESSAGE_PREFIX, plugin.getName());
    }
    public static Oni of(JavaPlugin plugin){
        Validate.notNull(plugin,"Plugin cannot be null");
        if(claimedOni.containsKey(plugin.getName())){
            return claimedOni.get(plugin.getName());
        }
        claimedOni.put(plugin.getName(),new Oni(plugin));
        return claimedOni.get(plugin.getName());
    }
    public Optional<PlayerOnion> onionOf(Player player){
        return PlayerOnion.of(player,this);
    }
    public Optional<PlayerOnion> onionOf(UUID player){
        return PlayerOnion.of(player,this);
    }
    public Optional<PlayerOnion> onionOf(String player){
        return PlayerOnion.of(player,this);
    }
}
