package io.ib67.oni.onion;

import io.ib67.oni.internal.mock.item.OniItemMeta;
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

/**
 * ItemMeta Onion. *Export Required*
 *
 * @see io.ib67.oni.internal.mock.item.OniItemMeta
 * @since 1.0
 */
@ApiStatus.Experimental
public class ItemMetaOnion extends OniItemMeta {
    public final Accessor oni;

    protected ItemMetaOnion(ItemMeta itemMeta) {
        super(itemMeta);
        oni = new Accessor(this);
    }

    public static ItemMetaOnion of(@NonNull ItemMeta itemMeta) {
        Validate.isTrue(!(itemMeta instanceof OniItemMeta), "ItemMetaOnion cant proxy another OniItemMeta!");
        return new ItemMetaOnion(itemMeta);
    }

    /**
     * **MENTION** It produce a Bukkit ItemMeta
     *
     * @return New itemMeta
     */
    @Override
    public @NotNull ItemMeta clone() {
        return realItemMeta.clone();
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
