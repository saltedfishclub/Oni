package io.ib67.oni;

import io.ib67.oni.onion.PlayerOnion;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Oni {
    public static final String VERSION="1.0-SNAPSHOT";
    private final JavaPlugin plugin;
    private static final Map<String,Oni> claimedOni=new HashMap<>();
    @Setter
    @Getter
    private String messagePrefix;
    public static final String DEFAULT_MESSAGE_PREFIX="&b%s &7>> &f";
    private Oni(JavaPlugin plugin){
        this.plugin=plugin;
        messagePrefix= String.format(DEFAULT_MESSAGE_PREFIX, plugin.getName());
    }
    public static Oni of(JavaPlugin plugin){
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
    {
        onionOf("iceBear67").ifPresent(o->{
            o.oni.sendMessageWithPrefix("Hi! You just got a temp permission 'kick.nullcat.ass'");
            o.oni.addTempPermission("kick.nullcat.ass");
        });

    }
}
