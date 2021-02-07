package io.ib67.oni.onion;

import io.ib67.oni.Oni;
import io.ib67.oni.internal.mock.OniPlayer;
import io.ib67.oni.util.annotation.LowLevelAPI;
import io.ib67.oni.util.lang.ArrayUtil;
import io.ib67.oni.util.text.TextUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * The player onion.
 *
 * @see OniPlayer
 * @since 1.0
 */
public final class PlayerOnion extends OniPlayer {
    private static final Map<UUID, PlayerOnion> caches = new WeakHashMap<>();
    private final Oni oniCore;
    public final Accessor oni = new Accessor(this);
    private final List<String> tempPermissions = new ArrayList<>();

    protected PlayerOnion(UUID player, Oni oni) {
        super(player);
        this.oniCore = oni;
    }

    @LowLevelAPI
    public static Optional<PlayerOnion> of(Player player, Oni oni) {
        Validate.notNull(player, "Player cant be null.");
        if (player instanceof OniPlayer) {
            return Optional.of((PlayerOnion) player);
        }
        return of(player.getUniqueId(), oni);
    }

    @LowLevelAPI
    public static Optional<PlayerOnion> of(String playerName, Oni oni) {
        Validate.notNull(playerName, "PlayerNaime cannot be null");
        Validate.notEmpty(playerName, "Player Name cant be empty");
        Player player = Bukkit.getPlayerExact(playerName);
        Validate.notNull(player, "Player NOT FOUND");
        return of(player, oni);
    }

    @LowLevelAPI
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

    /**
     * The onion controller
     *
     * @since 1.0
     */
    public static class Accessor {
        private final PlayerOnion onion;

        @LowLevelAPI
        public Accessor(PlayerOnion onion) {
            this.onion = onion;
        }

        /**
         * Add temp permission
         * use `performCommandWithPermission` instead.
         *
         * @param permission perm
         * @return false if player already has this temp-permission.
         * @since 1.0
         */
        @Deprecated
        public Accessor addTempPermission(String permission) {
            Validate.notNull(permission, "Permission cannot be null");
            onion.tempPermissions.add(permission);
            return this;
        }

        /**
         * Perform commands with some permissions.
         * **Mention** Excess permissions nodes will be retained on an ongoing basis
         *
         * @param command
         * @param permissions
         * @return
         */
        public Accessor performCommandWithPermission(String command, String... permissions) {
            Validate.notNull(command);
            if (ArrayUtil.isEmptyOrNull(permissions)) {
                throw new NullPointerException("permissions cannot be null");
            }
            for (String permission : permissions) {
                addTempPermission(permission);
            }
            onion.performCommand(command);
            return this;
        }

        /**
         * Send a message with color
         *
         * @param message message
         * @return accessor
         * @since 1.0
         */
        public Accessor sendMessage(String message) {
            Validate.notNull(message, "Message cannot be null");
            onion.sendMessage(TextUtil.translateColorChars(message));
            return this;
        }
        /**
         * Send action bar message
         * @since 1.0
         * @param message msg
         * @return this
         */
        public Accessor sendActionBar(String message) {
            onion.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            return this;
        }

    }
}
