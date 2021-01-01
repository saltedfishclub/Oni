package io.ib67.oni;

import io.ib67.oni.inject.Injector;
import io.ib67.oni.inject.OniInject;
import io.ib67.oni.onion.ItemOnion;
import io.ib67.oni.onion.PlayerOnion;
import io.ib67.oni.util.lang.ReflectUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

/**
 * Oni basic core.
 *
 * @since 1.0
 */
public class Oni {
    /**
     * SemVer
     */
    public static final String SEM_VERSION = "1.0-M1-dev-1";
    @Getter
    private final JavaPlugin plugin;
    private static final Map<String, Oni> claimedOni = new HashMap<>();
    @Setter
    @Getter
    private String messagePrefix;
    public static final String DEFAULT_MESSAGE_PREFIX = "&b%s &7>> &f";
    @Getter
    private static final List<URL> loadedDeps = new ArrayList<>();
    @Getter
    private static final Logger globalLogger = Logger.getLogger("Oni");
    private final Logger logger;

    static {
        Injector.INSTANCE.addFieldHandler(OniInject.class, p -> { //Inject Spreader
            Object o = ReflectUtil.getField(p.value, p.key);
            if (o == null) {
                return;
            }
            Injector.INSTANCE.process(o);
        });

    }

    private Oni(JavaPlugin plugin) {
        this.plugin = plugin;
        logger = Logger.getLogger(plugin.getName());
        messagePrefix = String.format(DEFAULT_MESSAGE_PREFIX, plugin.getName());

    }

    /**
     * Get a oni instance of your plugin.
     *
     * @param plugin TargetPlugin
     * @return Oni Instance
     * @since 1.0
     */
    public static Oni of(JavaPlugin plugin) {
        Validate.notNull(plugin, "Plugin cannot be null");
        if (claimedOni.containsKey(plugin.getName())) {
            return claimedOni.get(plugin.getName());
        }
        claimedOni.put(plugin.getName(), new Oni(plugin));
        return claimedOni.get(plugin.getName());
    }

    private static void initPlugin(JavaPlugin plugin) {

        Injector.INSTANCE.process(plugin);
    }

    /**
     * Get a Player onion of player
     *
     * @param player target player
     * @return onion
     * @since 1.0
     */
    public Optional<PlayerOnion> onionOf(Player player) {
        return PlayerOnion.of(player, this);
    }

    /**
     * Get a player onion by uuid
     *
     * @param player uuid
     * @return onion
     * @since 1.0
     */
    public Optional<PlayerOnion> onionOf(UUID player) {
        return PlayerOnion.of(player, this);
    }

    /**
     * get a player onion by name
     *
     * @param player name
     * @return onion
     * @since 1.0
     */
    public Optional<PlayerOnion> playerOnionOf(String player) {
        return PlayerOnion.of(player, this);
    }

    /**
     * get a itemOnion by itemStack
     *
     * @param itemStack wrap target
     * @return itemonion
     * @since 1.0
     */
    public ItemOnion onionOf(ItemStack itemStack) {
        return ItemOnion.of(itemStack);
    }

    /**
     * get a item onion by material
     *
     * @param itemMaterial material
     * @return Optional(ItemOnion) ,Empty when material is air
     */
    public Optional<ItemOnion> onionOf(Material itemMaterial) {
        switch (itemMaterial) {
            case AIR:
                return Optional.empty();
            default:
                return Optional.of(ItemOnion.of(itemMaterial));
        }
    }

}
