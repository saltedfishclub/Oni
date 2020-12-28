package io.ib67.oni.onion;

import io.ib67.oni.internal.mock.OniItem;
import io.ib67.oni.internal.mock.item.OniItemMeta;
import io.ib67.oni.util.annotation.DelegateMethod;
import io.ib67.oni.util.annotation.LowLevelAPI;
import io.ib67.oni.util.lang.ArrayUtil;
import lombok.NonNull;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

/**
 * **Mention** You have to `export()` the ItemStack out before leaking into BukkitAPI!
 *
 * @see io.ib67.oni.internal.mock.OniItem
 * @since 1.0
 */
@ApiStatus.Experimental
public class ItemOnion extends OniItem {
    public final Accessor oni;
    private ItemMetaOnion itemMeta;

    protected ItemOnion(ItemStack item) {
        super(item);
        oni = new Accessor(this);
        itemMeta = ItemMetaOnion.of(item.getItemMeta(), this);
    }

    /**
     * create itemonion
     *
     * @param itemStack target item
     * @return itemOnion
     * @since 1.0
     */
    @LowLevelAPI
    public static ItemOnion of(@NonNull ItemStack itemStack) {
        Validate.isTrue(!(itemStack instanceof OniItem), "ItemOnion cant proxy another oniItem!");
        return new ItemOnion(itemStack);
    }

    /**
     * create itemOnion,may produce unexpected error when material is air.
     *
     * @param material material
     * @return itemOnion
     * @since 1.0
     */
    public static ItemOnion of(@NonNull Material material) {
        return of(new ItemStack(material));
    }

    /**
     * Get OniItemMeta
     *
     * @return ItemMeta
     * @since 1.0
     */
    public ItemMetaOnion getItemMeta() {
        return itemMeta;
    }

    /**
     * Set oniItemMeta
     * possible fail if OIM internal IM isn't compat.
     *
     * @since 1.0
     * @deprecated use withMeta.
     */
    @LowLevelAPI
    public boolean setItemMeta(ItemMetaOnion im) {
        this.itemMeta = im;
        return super.setItemMeta(im.export());
    }

    /**
     * Set a bukkit itemMeta.
     * Will create a new OniItemMeta.
     * Possible fail
     *
     * @param im itemMeta
     * @since 1.0
     */
    @Override
    @LowLevelAPI
    public boolean setItemMeta(ItemMeta im) {
        if (im instanceof OniItemMeta) {
            return setItemMeta((ItemMetaOnion) im);
        } else {
            return setItemMeta(ItemMetaOnion.of(im, this));
        }
    }

    /**
     * set item name
     *
     * @since 1.0
     */
    @DelegateMethod(ItemMeta.class)
    public void setDisplayName(String name) {
        ItemMeta im = getItemMeta();
        im.setDisplayName(name);
    }

    /**
     * @return ItemStack's displayName
     * @since 1.0
     */
    @DelegateMethod(ItemMeta.class)
    public String getDisplayName() {
        return getItemMeta().getDisplayName();
    }

    /**
     * Add lore to itemMeta
     *
     * @return self
     * @since 1.0
     */
    @DelegateMethod(ItemMetaOnion.class)
    public ItemOnion addLore(String... lores) {
        if (ArrayUtil.isEmptyOrNull(lores)) return this;
        itemMeta.oni.addLore(lores);
        return this;
    }

    /**
     * Do something in lambda with itemMeta.
     *
     * @param context function
     * @return itemMeta
     */
    public ItemMetaOnion withMeta(Consumer<ItemMetaOnion> context) {
        context.accept(itemMeta);
        return itemMeta;
    }

    @Override
    public ItemStack export() {
        ItemStack itemStack = super.realItem.clone();
        itemStack.setItemMeta(itemMeta.export());
        return itemStack;
    }

    public static class Accessor {
        private ItemOnion onion;

        private Accessor(ItemOnion onion) {
            this.onion = onion;
        }

        public Object asNMSCopy() {
            //// TODO: 2020/12/28 Waiting for Oni:NMS Module.
            throw new UnsupportedOperationException("Not implemented.");
        }

        public Accessor send(Player... players) {
            if (players == null || players.length == 0) {
                return this;
            }
            for (Player player : players) {
                player.getInventory().addItem(onion.export());
            }
            return this;
        }

        public Accessor send(Inventory... inventories) {
            if (inventories == null || inventories.length == 0) {
                return this;
            }
            for (Inventory inventory : inventories) {
                inventory.addItem(onion.export());
            }
            return this;
        }


    }
}
