package io.ib67.oni.onion;

import io.ib67.oni.ConstPrefix;
import io.ib67.oni.Oni;
import io.ib67.oni.mock.OniPlayer;
import io.ib67.oni.util.text.TextUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public final class PlayerOnion extends OniPlayer {
    private static final Map<UUID, PlayerOnion> caches = new WeakHashMap<>();
    private final Oni oniCore;
    public final Accessor oni = new Accessor(this);
    private final List<String> tempPermissions = new ArrayList<>();

    protected PlayerOnion(UUID player, Oni oni) {
        super(player);
        this.oniCore = oni;
    }

    public static Optional<PlayerOnion> of(Player player, Oni oni) {
        Validate.notNull(player, "Player cant be null.");
        if (player instanceof OniPlayer) {
            return Optional.of((PlayerOnion) player);
        }
        return of(player.getUniqueId(), oni);
    }

    public static Optional<PlayerOnion> of(String playerName, Oni oni) {
        Validate.notNull(playerName, "PlayerNaime cannot be null");
        Validate.notEmpty(playerName, "Player Name cant be empty");
        Player player = Bukkit.getPlayerExact(playerName);
        Validate.notNull(player, "Player NOT FOUND");
        return of(player, oni);
    }

    public static Optional<PlayerOnion> of(UUID uuid, Oni oni) {
        Validate.notNull(uuid, "UUID CANNOT BE NULL!!");
        if (Bukkit.getPlayer(uuid) == null) {
            return Optional.empty();
        }
        caches.put(uuid, new PlayerOnion(uuid, oni));
        return Optional.of(caches.get(uuid));
    }
    //Function

    @Override
    public boolean hasPermission(String name) {
        return tempPermissions.contains(name) ? tempPermissions.remove(name) : super.hasPermission(name);
    }

    public static class Accessor {
        private final PlayerOnion onion;

        public Accessor(PlayerOnion onion) {
            this.onion = onion;
        }

        /**
         * Add temp permission
         *
         * @return false if player already has this temp-permission.
         */
        public Accessor addTempPermission(String permission) {
            Validate.notNull(permission, "Permission cannot be null");
            if (onion.tempPermissions.contains(permission)) {
                return this;
            }
            onion.tempPermissions.add(permission);
            return this;
        }

        /**
         * Send a message with color
         *
         * @param message
         */
        public Accessor sendMessage(String message) {
            Validate.notNull(message, "Message cannot be null");
            onion.sendMessage(TextUtil.translateColorChars(message));
            return this;
        }

        /**
         * Send message with prefix
         *
         * @param message
         */
        public Accessor info(String message) {
            Validate.notNull(message, "Message cannot be null");
            sendMessage(onion.oniCore.getMessagePrefix() + message);
            return this;
        }

        /**
         * Warn a player
         *
         * @param message
         * @return accessor
         */
        public Accessor warn(String message) {
            Validate.notNull(message, "Message cannot be null");
            sendMessage(ConstPrefix.WARN + message);
            return this;
        }

        /**
         * print fatal error
         *
         * @param message
         * @return
         */
        public Accessor fatal(String message) {
            Validate.notNull(message, "Message cannot be null");
            sendMessage(ConstPrefix.FATAL + message);
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "( FATAL ) " + message + " onion: " + onion.toString());
            onion.playSound(onion.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 5f, 3f);
            return this;
        }

        /**
         * Remind a player to do sth
         *
         * @param message
         * @return
         */
        public Accessor tip(String message) {
            Validate.notNull(message, "Message cannot be null");
            sendMessage(ConstPrefix.TIP + message);
            onion.playSound(onion.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1f);
            return this;
        }

        /**
         * Send action bar message
         *
         * @param message
         * @return
         */
        public Accessor sendActionBar(String message) {
            onion.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            return this;
        }

    }
}
