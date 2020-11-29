package io.ib67.oni.onion;

import io.ib67.oni.Oni;
import io.ib67.oni.util.text.TextUtil;
import io.ib67.oni.mock.OniPlayer;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public final class PlayerOnion extends OniPlayer{
    private static final Map<UUID,PlayerOnion> caches=new WeakHashMap<>();
    private final Oni oniCore;
    public final Accessor oni=new Accessor(this);
    private final List<String> tempPermissions=new ArrayList<>();
    protected PlayerOnion(UUID player, Oni oni) {
        super(player);
        this.oniCore=oni;
    }
    public static Optional<PlayerOnion> of(Player player,Oni oni){
        Validate.notNull(player,"Player cant be null.");
        if(player instanceof OniPlayer){
            return Optional.of((PlayerOnion) player);
        }
        return of(player.getUniqueId(),oni);
    }
    public static Optional<PlayerOnion> of(String playerName,Oni oni){
        Validate.notNull(playerName,"PlayerNaime cannot be null");
        Validate.notEmpty(playerName,"Player Name cant be empty");
        Player player= Bukkit.getPlayerExact(playerName);
        Validate.notNull(player,"Player NOT FOUND");
        return of(player,oni);
    }
    public static Optional<PlayerOnion> of(UUID uuid,Oni oni){
        Validate.notNull(uuid,"UUID CANNOT BE NULL!!");
        if(Bukkit.getPlayer(uuid)==null){
            return Optional.empty();
        }
        caches.put(uuid,new PlayerOnion(uuid,oni));
        return Optional.of(caches.get(uuid));
    }
    //Function

    @Override
    public boolean hasPermission(String name) {
        return tempPermissions.contains(name)?tempPermissions.remove(name):super.hasPermission(name);
    }

    public static class Accessor{
        private final PlayerOnion onion;
        public Accessor(PlayerOnion onion){
            this.onion=onion;
        }
        /**
         * Add temp permission
         * @return false if player already has this temp-permission.
         */
        public boolean addTempPermission(String permission){
            Validate.notNull(permission,"Permission cannot be null");
            if(onion.tempPermissions.contains(permission)){
                return false;
            }
            return onion.tempPermissions.add(permission);
        }

        /**
         * Send a message with pre-configured prefix in oni.
         * @param message
         */
        public void sendMessageWithPrefix(String message){
            Validate.notNull(message,"Message cannot be null");
            onion.sendMessage(TextUtil.translateColorChars(onion.oniCore.getMessagePrefix()+message));
        }
    }
}
