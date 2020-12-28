package io.ib67.oni.internal.mock;

import io.ib67.oni.internal.Exportable;
import org.bukkit.inventory.ItemStack;

/**
 * Oni Item Util
 *
 * @since 1.0
 */
public abstract class OniItem extends ItemStack implements Exportable<ItemStack> {
    protected final ItemStack realItem;

    protected OniItem(ItemStack item) {
        this.realItem = item;
    }

    public abstract ItemStack export();
}
