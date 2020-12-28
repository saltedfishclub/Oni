package io.ib67.oni.internal.mock.item;

import com.google.common.collect.Multimap;
import io.ib67.oni.internal.Exportable;
import io.ib67.oni.util.annotation.LowLevelAPI;
import org.apache.commons.lang.Validate;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * **ATTENTION** OniItemMeta CANNOT BE SERIALIZED BY `serialize()` DIRECTLY
 * About serialization for OIM,See those implements.
 *
 * @since 1.0
 */
@DelegateDeserialization(ItemMeta.class)
@ApiStatus.Internal
public abstract class OniItemMeta implements ItemMeta, Exportable<ItemMeta> {
    protected final ItemMeta realItemMeta;

    @LowLevelAPI
    @ApiStatus.Internal
    protected OniItemMeta(ItemMeta itemMeta) {
        Validate.notNull(itemMeta, "ItemMeta CANNOT BE NULL");
        this.realItemMeta = itemMeta;
    }

    @Override
    public ItemMeta export() {
        return realItemMeta;
    }

    @Override
    public int hashCode() {
        return realItemMeta.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return realItemMeta.equals(o);
    }

    @Override
    public String toString() {
        return realItemMeta.toString();
    }

    @Override
    public boolean hasDisplayName() {
        return realItemMeta.hasDisplayName();
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return realItemMeta.getDisplayName();
    }

    @Override
    public void setDisplayName(@Nullable String s) {
        realItemMeta.setDisplayName(s);
    }

    @Override
    public boolean hasLocalizedName() {
        return realItemMeta.hasLocalizedName();
    }

    @NotNull
    @Override
    public String getLocalizedName() {
        return realItemMeta.getLocalizedName();
    }

    @Override
    public void setLocalizedName(@Nullable String s) {
        realItemMeta.setLocalizedName(s);
    }

    @Override
    public boolean hasLore() {
        return realItemMeta.hasLore();
    }

    @Nullable
    @Override
    public List<String> getLore() {
        return realItemMeta.getLore();
    }

    @Override
    public void setLore(@Nullable List<String> list) {
        realItemMeta.setLore(list);
    }

    @Override
    public boolean hasCustomModelData() {
        return realItemMeta.hasCustomModelData();
    }

    @Override
    public int getCustomModelData() {
        return realItemMeta.getCustomModelData();
    }

    @Override
    public void setCustomModelData(@Nullable Integer integer) {
        realItemMeta.setCustomModelData(integer);
    }

    @Override
    public boolean hasEnchants() {
        return realItemMeta.hasEnchants();
    }

    @Override
    public boolean hasEnchant(@NotNull Enchantment enchantment) {
        return realItemMeta.hasEnchant(enchantment);
    }

    @Override
    public int getEnchantLevel(@NotNull Enchantment enchantment) {
        return realItemMeta.getEnchantLevel(enchantment);
    }

    @NotNull
    @Override
    public Map<Enchantment, Integer> getEnchants() {
        return realItemMeta.getEnchants();
    }

    @Override
    public boolean addEnchant(@NotNull Enchantment enchantment, int i, boolean b) {
        return realItemMeta.addEnchant(enchantment, i, b);
    }

    @Override
    public boolean removeEnchant(@NotNull Enchantment enchantment) {
        return realItemMeta.removeEnchant(enchantment);
    }

    @Override
    public boolean hasConflictingEnchant(@NotNull Enchantment enchantment) {
        return realItemMeta.hasConflictingEnchant(enchantment);
    }

    @Override
    public void addItemFlags(@NotNull ItemFlag... itemFlags) {
        realItemMeta.addItemFlags(itemFlags);
    }

    @Override
    public void removeItemFlags(@NotNull ItemFlag... itemFlags) {
        realItemMeta.removeItemFlags(itemFlags);
    }

    @NotNull
    @Override
    public Set<ItemFlag> getItemFlags() {
        return realItemMeta.getItemFlags();
    }

    @Override
    public boolean hasItemFlag(@NotNull ItemFlag itemFlag) {
        return realItemMeta.hasItemFlag(itemFlag);
    }

    @Override
    public boolean isUnbreakable() {
        return realItemMeta.isUnbreakable();
    }

    @Override
    public void setUnbreakable(boolean b) {
        realItemMeta.setUnbreakable(b);
    }

    @Override
    public boolean hasAttributeModifiers() {
        return realItemMeta.hasAttributeModifiers();
    }

    @Nullable
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return realItemMeta.getAttributeModifiers();
    }

    @NotNull
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        return realItemMeta.getAttributeModifiers(equipmentSlot);
    }

    @Nullable
    @Override
    public Collection<AttributeModifier> getAttributeModifiers(@NotNull Attribute attribute) {
        return realItemMeta.getAttributeModifiers(attribute);
    }

    @Override
    public boolean addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier attributeModifier) {
        return realItemMeta.addAttributeModifier(attribute, attributeModifier);
    }

    @Override
    public void setAttributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> multimap) {
        realItemMeta.setAttributeModifiers(multimap);
    }

    @Override
    public boolean removeAttributeModifier(@NotNull Attribute attribute) {
        return realItemMeta.removeAttributeModifier(attribute);
    }

    @Override
    public boolean removeAttributeModifier(@NotNull EquipmentSlot equipmentSlot) {
        return realItemMeta.removeAttributeModifier(equipmentSlot);
    }

    @Override
    public boolean removeAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier attributeModifier) {
        return realItemMeta.removeAttributeModifier(attribute, attributeModifier);
    }

    /**
     * @deprecated
     */
    @NotNull
    @Override
    @Deprecated
    public CustomItemTagContainer getCustomTagContainer() {
        return realItemMeta.getCustomTagContainer();
    }

    /**
     * @param i ver
     * @deprecated
     */
    @Deprecated
    @Override
    public void setVersion(int i) {
        realItemMeta.setVersion(i);
    }

    /**
     * **MENTION** ItemMeta may leak if impl can't deal it well.
     *
     * @return New OniItemMeta
     */
    @NotNull
    @Override
    public abstract ItemMeta clone();

    @NotNull
    @Override
    public Spigot spigot() {
        return realItemMeta.spigot();
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return realItemMeta.serialize();
    }

    @NotNull
    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return realItemMeta.getPersistentDataContainer();
    }
}
