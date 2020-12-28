package io.ib67.oni.onion;

import io.ib67.oni.internal.mock.item.OniItemMeta;
import io.ib67.oni.util.annotation.LowLevelAPI;
import io.ib67.oni.util.lang.ArrayUtil;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * ItemMeta Onion. *Export Required*
 *
 * @see io.ib67.oni.internal.mock.item.OniItemMeta
 * @since 1.0
 */
@ApiStatus.Experimental
public class ItemMetaOnion extends OniItemMeta {
    public final Accessor oni;
    private final ItemOnion bind;

    protected ItemMetaOnion(ItemMeta itemMeta, ItemOnion bind) {
        super(itemMeta);
        oni = new Accessor(this);
        this.bind = bind;
    }

    /**
     * Create a itemMeta Onion
     *
     * @param itemMeta im
     * @param bind     belongs
     * @return oim
     * @since 1.0
     */
    @LowLevelAPI
    public static ItemMetaOnion of(@NonNull ItemMeta itemMeta, ItemOnion bind) {
        Validate.isTrue(!(itemMeta instanceof OniItemMeta), "ItemMetaOnion cant proxy another OniItemMeta!");
        Validate.notNull(bind, "Bind shouldn't be null");
        return new ItemMetaOnion(itemMeta, bind);
    }

    /**
     * **MENTION** It produce a Bukkit ItemMeta
     *
     * @return New itemMeta
     * @since 1.0
     */
    @Override
    public @NotNull ItemMeta clone() {
        return realItemMeta.clone();
    }

    /**
     * Return the itemOnion which is this meta belonging to
     *
     * @return ItemOnion
     * @since 1.0
     */
    public @NotNull ItemOnion backToItem() {
        return bind;
    }

    /**
     * Seems like Item.withMeta.
     *
     * @param func func
     * @return backToItem()
     * @since 1.0
     */
    public ItemOnion withItem(Consumer<ItemOnion> func) {
        func.accept(bind);
        return bind;
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Accessor {
        private final ItemMetaOnion onion;

        /**
         * FluentAPI for add lore.
         *
         * @param lore lore
         * @return this
         */
        public Accessor addLore(String... lore) {
            if (ArrayUtil.isEmptyOrNull(lore)) return this;
            List<String> originalLore = onion.getLore();
            originalLore.addAll(Arrays.asList(lore));
            onion.setLore(originalLore);
            return this;
        }
    }
}
