package io.ib67.oni.mock;

import io.ib67.oni.exception.player.PlayerOfflineException;
import io.ib67.oni.onion.PlayerOnion;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.util.*;

@SuppressWarnings("all")
public abstract class OniPlayer implements Player {
    private final UUID player;
    private final String cachedName;
    private WeakReference<Player> playerRefs;
    protected OniPlayer(UUID player){
        this.player=player;
        this.cachedName= getRealPlayer().getName();
    }
    public boolean isOffline() {
        return getRealPlayerWithoutCheck() == null;
    }
    @Nullable
    public Player getRealPlayerWithoutCheck(){
        boolean checked=false;
        if(playerRefs==null){
            playerRefs=new WeakReference<>(Bukkit.getPlayer(this.player));
            checked=true;
        }
        Player player=playerRefs.get();
        if(player==null && !checked){
            playerRefs=new WeakReference<>(Bukkit.getPlayer(this.player));
        }
        return player;
    }
    @NonNull
    public Player getRealPlayer(){
        if(isOffline()){
            throwOfflineException();
        }
        return getRealPlayer();
    }
    @NonNull
    public OfflinePlayer asOfflinePlayer(){
        return Bukkit.getOfflinePlayer(player);
    }
    @SneakyThrows
    private void throwOfflineException(){
        throw new PlayerOfflineException(this.toString()+"is offline!");
    }
    @Override
    public String getDisplayName(){
        return getRealPlayer().getDisplayName();
    }

    @Override
    public void setDisplayName(String name) {
        getRealPlayer().setDisplayName(name);
    }

    @Override
    public String getPlayerListName() {
        return getRealPlayer().getPlayerListName();
    }

    @Override
    public void setPlayerListName(String name) {
        
        getRealPlayer().setPlayerListName(name);
    }

    @Override
    public String getPlayerListHeader() {
        
        return getRealPlayer().getPlayerListHeader();
    }

    @Override
    public String getPlayerListFooter() {
        
        return getRealPlayer().getPlayerListFooter();
    }

    @Override
    public void setPlayerListHeader(String header) {
        
        getRealPlayer().setPlayerListHeader(header);
    }

    @Override
    public void setPlayerListFooter(String footer) {
        
        getRealPlayer().setPlayerListFooter(footer);
    }

    @Override
    public void setPlayerListHeaderFooter(String header, String footer) {
        
        getRealPlayer().setPlayerListHeaderFooter(header,footer);
    }

    @Override
    public void setCompassTarget(Location loc) {
        
        getRealPlayer().setCompassTarget(loc);
    }

    @Override
    public Location getCompassTarget() {
        
        return getRealPlayer().getCompassTarget();
    }

    @Override
    public InetSocketAddress getAddress() {
        
        return getRealPlayer().getAddress();
    }

    @Override
    public boolean isConversing() {
        
        return getRealPlayer().isConversing();
    }

    @Override
    public void acceptConversationInput(String input) {
        
        getRealPlayer().acceptConversationInput(input);
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        
        return getRealPlayer().beginConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        
        getRealPlayer().abandonConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        
        getRealPlayer().abandonConversation(conversation, details);
    }

    @Override
    public void sendRawMessage(String message) {
        
        getRealPlayer().sendRawMessage(message);
    }

    @Override
    public void kickPlayer(String message) {
        
        getRealPlayer().kickPlayer(message);
    }

    @Override
    public void chat(String msg) {
        
        getRealPlayer().chat(msg);
    }

    @Override
    public boolean performCommand(String command) {
        
        return getRealPlayer().performCommand(command);
    }

    @Override
    public boolean isSneaking() {
        
        return getRealPlayer().isSneaking();
    }

    @Override
    public void setSneaking(boolean sneak) {
        
        getRealPlayer().setSneaking(sneak);
    }

    @Override
    public boolean isSprinting() {
        
        return getRealPlayer().isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        
        getRealPlayer().setSprinting(sprinting);
    }

    @Override
    public void saveData() {
        getRealPlayer().saveData();
    }

    @Override
    public void loadData() {
        getRealPlayer().loadData();
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        
        getRealPlayer().setSleepingIgnored(isSleeping);
    }

    @Override
    public boolean isSleepingIgnored() {
        return getRealPlayer().isSleepingIgnored();
    }

    @Deprecated
    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        getRealPlayer().playNote(loc,instrument,note);
    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        getRealPlayer().playNote(loc,instrument,note);
    }

    @Override
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        getRealPlayer().playSound(location, sound, volume, pitch);
    }

    @Override
    public void playSound(Location location, String sound, float volume, float pitch) {
        getRealPlayer().playSound(location, sound, volume, pitch);
    }

    @Override
    public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {
        getRealPlayer().playSound(location, sound, category, volume, pitch);
    }
    @Override
    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {
        getRealPlayer().playSound(location, sound, category, volume, pitch);
    }

    @Override
    public void stopSound(Sound sound) {
        getRealPlayer().stopSound(sound);
    }
    @Override
    public void stopSound(String sound) {
        getRealPlayer().stopSound(sound);
    }

    @Override
    public void stopSound(Sound sound, SoundCategory category) {
        getRealPlayer().stopSound(sound, category);
    }

    @Override
    public void stopSound(String sound, SoundCategory category) {
        getRealPlayer().stopSound(sound, category);
    }
    @Deprecated
    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        getRealPlayer().playEffect(loc,effect,data);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        getRealPlayer().playEffect(loc,effect,data);
    }

    @Deprecated
    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {
        getRealPlayer().sendBlockChange(loc,material,data);
    }

    @Override
    public void sendBlockChange(Location loc, BlockData block) {
        getRealPlayer().sendBlockChange(loc,block);
    }
    @Deprecated
    @Override
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        return getRealPlayer().sendChunkChange(loc, sx, sy, sz, data);
    }

    @Override
    public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException {
        getRealPlayer().sendSignChange(loc,lines);
    }

    @Override
    public void sendSignChange(Location loc, String[] lines, DyeColor dyeColor) throws IllegalArgumentException {
        getRealPlayer().sendSignChange(loc, lines, dyeColor);
    }

    @Override
    public void sendMap(MapView map) {
        getRealPlayer().sendMap(map);
    }

    @Override
    public void updateInventory() {
        getRealPlayer().updateInventory();
    }

    @Deprecated
    @Override
    public void awardAchievement(Achievement achievement) {
        getRealPlayer().awardAchievement(achievement);
    }

    @Override
    @Deprecated
    public void removeAchievement(Achievement achievement) {
        getRealPlayer().removeAchievement(achievement);
    }

    @Override
    @Deprecated
    public boolean hasAchievement(Achievement achievement) {
        return getRealPlayer().hasAchievement(achievement);
    }

    @Override
    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
        getRealPlayer().incrementStatistic(statistic);
    }

    @Override
    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        getRealPlayer().decrementStatistic(statistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        getRealPlayer().incrementStatistic(statistic, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        getRealPlayer().decrementStatistic(statistic, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {
        getRealPlayer().setStatistic(statistic, newValue);
    }

    @Override
    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        return getRealPlayer().getStatistic(statistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        getRealPlayer().incrementStatistic(statistic, material);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        getRealPlayer().decrementStatistic(statistic, material);
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        return getRealPlayer().getStatistic(statistic, material);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        getRealPlayer().incrementStatistic(statistic, material, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        getRealPlayer().decrementStatistic(statistic, material, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {
        getRealPlayer().setStatistic(statistic, material, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        getRealPlayer().incrementStatistic(statistic, entityType);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        getRealPlayer().decrementStatistic(statistic, entityType);
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        return getRealPlayer().getStatistic(statistic, entityType);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {
        getRealPlayer().incrementStatistic(statistic, entityType, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        getRealPlayer().decrementStatistic(statistic, entityType, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        getRealPlayer().setStatistic(statistic, entityType, newValue);
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        getRealPlayer().setPlayerTime(time,relative);
    }

    @Override
    public long getPlayerTime() {
        return getRealPlayer().getPlayerTime();
    }

    @Override
    public long getPlayerTimeOffset() {
        return getRealPlayer().getPlayerTimeOffset();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return getRealPlayer().isPlayerTimeRelative();
    }

    @Override
    public void resetPlayerTime() {
        getRealPlayer().resetPlayerTime();
    }

    @Override
    public void setPlayerWeather(WeatherType type) {
        getRealPlayer().setPlayerWeather(type);
    }

    @Override
    public WeatherType getPlayerWeather() {
        return getRealPlayer().getPlayerWeather();
    }

    @Override
    public void resetPlayerWeather() {
        getRealPlayer().resetPlayerWeather();
    }

    @Override
    public void giveExp(int amount) {
        getRealPlayer().giveExp(amount);
    }

    @Override
    public void giveExpLevels(int amount) {
        getRealPlayer().giveExpLevels(amount);
    }

    @Override
    public float getExp() {
        return getRealPlayer().getExp();
    }

    @Override
    public void setExp(float exp) {
        getRealPlayer().setExp(exp);
    }

    @Override
    public int getLevel() {
        return getRealPlayer().getLevel();
    }

    @Override
    public void setLevel(int level) {
        getRealPlayer().setLevel(level);
    }

    @Override
    public int getTotalExperience() {
        return getRealPlayer().getTotalExperience();
    }

    @Override
    public void setTotalExperience(int exp) {
        getRealPlayer().setTotalExperience(exp);
    }

    @Override
    public float getExhaustion() {
        return getRealPlayer().getExhaustion();
    }

    @Override
    public void setExhaustion(float value) {
        getRealPlayer().setExhaustion(value);
    }

    @Override
    public float getSaturation() {
        return getRealPlayer().getSaturation();
    }

    @Override
    public void setSaturation(float value) {
        getRealPlayer().setSaturation(value);
    }

    @Override
    public int getFoodLevel() {
        return getRealPlayer().getFoodLevel();
    }

    @Override
    public void setFoodLevel(int value) {
        getRealPlayer().setFoodLevel(value);
    }

    @Override
    public boolean getAllowFlight() {
        return getRealPlayer().getAllowFlight();
    }

    @Override
    public void setAllowFlight(boolean flight) {
        getRealPlayer().setAllowFlight(flight);
    }

    @Deprecated
    @Override
    public void hidePlayer(Player player) {
        getRealPlayer().hidePlayer(player);
    }

    @Override
    public void hidePlayer(Plugin plugin, Player player) {
        getRealPlayer().hidePlayer(plugin, player);
    }

    @Override
    @Deprecated
    public void showPlayer(Player player) {
        getRealPlayer().showPlayer(player);
    }

    @Override
    public void showPlayer(Plugin plugin, Player player) {
        getRealPlayer().showPlayer(plugin,player);
    }

    @Override
    public boolean canSee(Player player) {
        return getRealPlayer().canSee(player);
    }

    @Override
    public boolean isFlying() {
        return getRealPlayer().isFlying();
    }

    @Override
    public void setFlying(boolean value) {
        getRealPlayer().setFlying(value);
    }

    @Override
    public void setFlySpeed(float value) throws IllegalArgumentException {
        getRealPlayer().setFlySpeed(value);
    }

    @Override
    public void setWalkSpeed(float value) throws IllegalArgumentException {
        getRealPlayer().setWalkSpeed(value);
    }

    @Override
    public float getFlySpeed() {
        return getRealPlayer().getFlySpeed();
    }

    @Override
    public float getWalkSpeed() {
        return getRealPlayer().getWalkSpeed();
    }

    @Override
    @Deprecated
    public void setTexturePack(String url) {
        getRealPlayer().setTexturePack(url);
    }

    @Override
    public void setResourcePack(String url) {
        getRealPlayer().setResourcePack(url);
    }

    @Override
    public void setResourcePack(String url, byte[] hash) {
        getRealPlayer().setResourcePack(url, hash);
    }

    @Override
    public Scoreboard getScoreboard() {
        return getRealPlayer().getScoreboard();
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        getRealPlayer().setScoreboard(scoreboard);
    }

    @Override
    public boolean isHealthScaled() {
        return getRealPlayer().isHealthScaled();
    }

    @Override
    public void setHealthScaled(boolean scale) {
        getRealPlayer().setHealthScaled(scale);
    }

    @Override
    public void setHealthScale(double scale) throws IllegalArgumentException {
        getRealPlayer().setHealthScale(scale);
    }

    @Override
    public double getHealthScale() {
        return getRealPlayer().getHealthScale();
    }

    @Override
    public Entity getSpectatorTarget() {
        return getRealPlayer().getSpectatorTarget();
    }

    @Override
    public void setSpectatorTarget(Entity entity) {
        getRealPlayer().setSpectatorTarget(entity);
    }

    @Override
    @Deprecated
    public void sendTitle(String title, String subtitle) {
        getRealPlayer().sendTitle(title,subtitle);
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        getRealPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void resetTitle() {
        getRealPlayer().resetTitle();
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {
        getRealPlayer().spawnParticle(particle, location, count);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {
        getRealPlayer().spawnParticle(particle, x, y, z, count);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        getRealPlayer().spawnParticle(particle, location, count, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        getRealPlayer().spawnParticle(particle, x, y, z, count, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        getRealPlayer().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        getRealPlayer().spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        getRealPlayer().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        getRealPlayer().spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        getRealPlayer().spawnParticle(particle,location,count,offsetX,offsetY,offsetZ,extra);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        getRealPlayer().spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        getRealPlayer().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        getRealPlayer().spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public AdvancementProgress getAdvancementProgress(Advancement advancement) {
        return getRealPlayer().getAdvancementProgress(advancement);
    }

    @Override
    public int getClientViewDistance() {
        return getRealPlayer().getClientViewDistance();
    }

    @Override
    public String getLocale() {
        return getRealPlayer().getLocale();
    }

    @Override
    public void updateCommands() {
        getRealPlayer().updateCommands();
    }

    @Override
    public void openBook(ItemStack book) {
        getRealPlayer().openBook(book);
    }

    @Override
    public Location getLocation() {
        return getRealPlayer().getLocation();
    }

    @Override
    public Location getLocation(Location loc) {
        return getRealPlayer().getLocation(loc);
    }

    @Override
    public void setVelocity(Vector velocity) {
        getRealPlayer().setVelocity(velocity);
    }

    @Override
    public Vector getVelocity() {
        return getRealPlayer().getVelocity();
    }

    @Override
    public double getHeight() {
        return getRealPlayer().getHeight();
    }

    @Override
    public double getWidth() {
        return getRealPlayer().getWidth();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getRealPlayer().getBoundingBox();
    }

    @Override
    public boolean isOnGround() {
        return getRealPlayer().isOnGround();
    }

    @Override
    public World getWorld() {
        return getRealPlayer().getWorld();
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        getRealPlayer().setRotation(yaw,pitch);
    }

    @Override
    public boolean teleport(Location location) {
        return getRealPlayer().teleport(location);
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        return getRealPlayer().teleport(location, cause);
    }

    @Override
    public boolean teleport(Entity destination) {
        return getRealPlayer().teleport(destination);
    }

    @Override
    public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause) {
        return getRealPlayer().teleport(destination, cause);
    }

    @Override
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        return getRealPlayer().getNearbyEntities(x,y,z);
    }

    @Override
    public int getEntityId() {
        return getRealPlayer().getEntityId();
    }

    @Override
    public int getFireTicks() {
        return getRealPlayer().getFireTicks();
    }

    @Override
    public int getMaxFireTicks() {
        return getRealPlayer().getMaxFireTicks();
    }

    @Override
    public void setFireTicks(int ticks) {
        getRealPlayer().setFireTicks(ticks);
    }

    @Override
    public void remove() {
        getRealPlayer().remove();
    }

    @Override
    public boolean isDead() {
        return getRealPlayer().isDead();
    }

    @Override
    public boolean isValid() {
        return getRealPlayer().isValid();
    }

    @Override
    public void sendMessage(String message) {
        getRealPlayer().sendMessage(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        getRealPlayer().sendMessage(messages);
    }

    @Override
    @Deprecated
    public Server getServer() {
        return getRealPlayer().getServer();
    }

    @Override
    @Deprecated
    public boolean isPersistent() {
        return getRealPlayer().isPersistent();
    }

    @Override
    @Deprecated
    public void setPersistent(boolean persistent) {
        getRealPlayer().setPersistent(persistent);
    }

    @Override
    @Deprecated
    public Entity getPassenger() {
        return getRealPlayer().getPassenger();
    }

    @Override
    @Deprecated
    public boolean setPassenger(Entity passenger) {
        return getRealPlayer().setPassenger(passenger);
    }

    @Override
    public List<Entity> getPassengers() {
        return getRealPlayer().getPassengers();
    }

    @Override
    public boolean addPassenger(Entity passenger) {
        return getRealPlayer().addPassenger(passenger);
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        return getRealPlayer().removePassenger(passenger);
    }

    @Override
    public boolean isEmpty() {
        return getRealPlayer().isEmpty();
    }

    @Override
    public boolean eject() {
        return getRealPlayer().eject();
    }

    @Override
    public float getFallDistance() {
        return getRealPlayer().getFallDistance();
    }

    @Override
    public void setFallDistance(float distance) {
        getRealPlayer().setFallDistance(distance);
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        getRealPlayer().setLastDamageCause(event);
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return getRealPlayer().getLastDamageCause();
    }

    @Override
    @NonNull
    public UUID getUniqueId() {
        return player;
    }

    @Override
    public int getTicksLived() {
        return getRealPlayer().getTicksLived();
    }

    @Override
    public void setTicksLived(int value) {
        getRealPlayer().setTicksLived(value);
    }

    @Override
    public void playEffect(EntityEffect type) {
        getRealPlayer().playEffect(type);
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public boolean isInsideVehicle() {
        return getRealPlayer().isInsideVehicle();
    }

    @Override
    public boolean leaveVehicle() {
        return getRealPlayer().leaveVehicle();
    }

    @Override
    @Nullable
    public Entity getVehicle() {
        return getRealPlayer().getVehicle();
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        getRealPlayer().setCustomNameVisible(flag);
    }

    @Override
    public boolean isCustomNameVisible() {
        return getRealPlayer().isCustomNameVisible();
    }

    @Override
    public void setGlowing(boolean flag) {
        getRealPlayer().setGlowing(flag);
    }

    @Override
    public boolean isGlowing() {
        return getRealPlayer().isGlowing();
    }

    @Override
    public void setInvulnerable(boolean flag) {
        getRealPlayer().setInvulnerable(flag);
    }

    @Override
    public boolean isInvulnerable() {
        return getRealPlayer().isInvulnerable();
    }

    @Override
    public boolean isSilent() {
        return getRealPlayer().isSilent();
    }

    @Override
    public void setSilent(boolean flag) {
        getRealPlayer().setSilent(flag);
    }

    @Override
    public boolean hasGravity() {
        return getRealPlayer().hasGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        getRealPlayer().setGravity(gravity);
    }

    @Override
    public int getPortalCooldown() {
        return getRealPlayer().getPortalCooldown();
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        getRealPlayer().setPortalCooldown(cooldown);
    }

    @Override
    public Set<String> getScoreboardTags() {
        return getRealPlayer().getScoreboardTags();
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        return getRealPlayer().addScoreboardTag(tag);
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        return getRealPlayer().removeScoreboardTag(tag);
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return getRealPlayer().getPistonMoveReaction();
    }

    @Override
    public BlockFace getFacing() {
        return getRealPlayer().getFacing();
    }

    @Override
    public Pose getPose() {
        return getRealPlayer().getPose();
    }

    @Override
    public Spigot spigot() {
        return getRealPlayer().spigot();
    }

    @Override
    public boolean isOnline() {
        return asOfflinePlayer().isOnline();
    }

    @Override
    public boolean isBanned() {
        return asOfflinePlayer().isBanned();
    }

    @Override
    public boolean isWhitelisted() {
        return asOfflinePlayer().isWhitelisted();
    }

    @Override
    public void setWhitelisted(boolean value) {
        asOfflinePlayer().setWhitelisted(value);
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    @Override
    public long getFirstPlayed() {
        return asOfflinePlayer().getFirstPlayed();
    }

    @Override
    public long getLastPlayed() {
        return asOfflinePlayer().getLastPlayed();
    }

    @Override
    public boolean hasPlayedBefore() {
        return asOfflinePlayer().hasPlayedBefore();
    }

    @Override
    public Map<String, Object> serialize() {
        return getRealPlayer().serialize();
    }

    @Override
    public String getName() {
        return isOffline()?cachedName:getRealPlayer().getName();
    }

    @Override
    public PlayerInventory getInventory() {
        return getRealPlayer().getInventory();
    }

    @Override
    public Inventory getEnderChest() {
        return getRealPlayer().getEnderChest();
    }

    @Override
    public MainHand getMainHand() {
        return getRealPlayer().getMainHand();
    }

    @Override
    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        return getRealPlayer().setWindowProperty(prop, value);
    }

    @Override
    public InventoryView getOpenInventory() {
        return getRealPlayer().getOpenInventory();
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        return getRealPlayer().openInventory(inventory);
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        return getRealPlayer().openWorkbench(location, force);
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        return getRealPlayer().openEnchanting(location, force);
    }

    @Override
    public void openInventory(InventoryView inventory) {
        getRealPlayer().openInventory(inventory);
    }

    @Override
    public InventoryView openMerchant(Villager trader, boolean force) {
        return getRealPlayer().openMerchant(trader,force);
    }

    @Override
    public InventoryView openMerchant(Merchant merchant, boolean force) {
        return getRealPlayer().openMerchant(merchant,force);
    }

    @Override
    public void closeInventory() {
        getRealPlayer().closeInventory();
    }

    @Override
    @Deprecated
    @Nullable
    public ItemStack getItemInHand() {
        return getRealPlayer().getItemInHand();
    }

    @Override
    @Deprecated
    public void setItemInHand(ItemStack item) {
        getRealPlayer().setItemInHand(item);
    }

    @Override
    @Nullable
    public ItemStack getItemOnCursor() {
        return getRealPlayer().getItemOnCursor();
    }

    @Override
    public void setItemOnCursor(ItemStack item) {
        getRealPlayer().setItemOnCursor(item);
    }

    @Override
    public boolean hasCooldown(Material material) {
        return getRealPlayer().hasCooldown(material);
    }

    @Override
    public int getCooldown(Material material) {
        return getRealPlayer().getCooldown(material);
    }

    @Override
    public void setCooldown(Material material, int ticks) {
        getRealPlayer().setCooldown(material,ticks);
    }

    @Override
    public int getSleepTicks() {
        return getRealPlayer().getSleepTicks();
    }

    @Override
    public Location getBedSpawnLocation() {
        return isOffline()?asOfflinePlayer().getBedSpawnLocation():getRealPlayer().getBedSpawnLocation();
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        getRealPlayer().setBedSpawnLocation(location);
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean force) {
        getRealPlayer().setBedSpawnLocation(location, force);
    }

    @Override
    public boolean sleep(Location location, boolean force) {
        return getRealPlayer().sleep(location, force);
    }

    @Override
    public void wakeup(boolean setSpawnLocation) {
        getRealPlayer().wakeup(setSpawnLocation);
    }

    @Override
    public Location getBedLocation() {
        return getRealPlayer().getBedLocation();
    }

    @Override
    public GameMode getGameMode() {
        return getRealPlayer().getGameMode();
    }

    @Override
    public void setGameMode(GameMode mode) {
        getRealPlayer().setGameMode(mode);
    }

    @Override
    public boolean isBlocking() {
        return getRealPlayer().isBlocking();
    }

    @Override
    public boolean isHandRaised() {
        return getRealPlayer().isHandRaised();
    }

    @Override
    public int getExpToLevel() {
        return getRealPlayer().getExpToLevel();
    }

    @Override
    public boolean discoverRecipe(NamespacedKey recipe) {
        return getRealPlayer().discoverRecipe(recipe);
    }

    @Override
    public int discoverRecipes(Collection<NamespacedKey> recipes) {
        return getRealPlayer().discoverRecipes(recipes);
    }

    @Override
    public boolean undiscoverRecipe(NamespacedKey recipe) {
        return getRealPlayer().undiscoverRecipe(recipe);
    }

    @Override
    public int undiscoverRecipes(Collection<NamespacedKey> recipes) {
        return getRealPlayer().undiscoverRecipes(recipes);
    }

    @Override
    @Deprecated
    @Nullable
    public Entity getShoulderEntityLeft() {
        return getRealPlayer().getShoulderEntityLeft();
    }

    @Override
    @Deprecated
    public void setShoulderEntityLeft(Entity entity) {
        getRealPlayer().setShoulderEntityLeft(entity);
    }

    @Override
    @Deprecated
    @Nullable
    public Entity getShoulderEntityRight() {
        return getRealPlayer().getShoulderEntityRight();
    }

    @Override
    @Deprecated
    public void setShoulderEntityRight(Entity entity) {
        getRealPlayer().setShoulderEntityRight(entity);
    }

    @Override
    public double getEyeHeight() {
        return getRealPlayer().getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return getRealPlayer().getEyeHeight(ignorePose);
    }

    @Override
    public Location getEyeLocation() {
        return getRealPlayer().getEyeLocation();
    }

    @Override
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        return getRealPlayer().getLineOfSight(transparent, maxDistance);
    }

    @Override
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        return getRealPlayer().getTargetBlock(transparent, maxDistance);
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        return getRealPlayer().getLastTwoTargetBlocks(transparent, maxDistance);
    }

    @Override
    public Block getTargetBlockExact(int maxDistance) {
        return getRealPlayer().getTargetBlockExact(maxDistance);
    }

    @Override
    public Block getTargetBlockExact(int maxDistance, FluidCollisionMode fluidCollisionMode) {
        return getRealPlayer().getTargetBlockExact(maxDistance, fluidCollisionMode);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance) {
        return getRealPlayer().rayTraceBlocks(maxDistance);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance, FluidCollisionMode fluidCollisionMode) {
        return getRealPlayer().rayTraceBlocks(maxDistance, fluidCollisionMode);
    }

    @Override
    public int getRemainingAir() {
        return getRealPlayer().getRemainingAir();
    }

    @Override
    public void setRemainingAir(int ticks) {
        getRealPlayer().setRemainingAir(ticks);
    }

    @Override
    public int getMaximumAir() {
        return getRealPlayer().getMaximumAir();
    }

    @Override
    public void setMaximumAir(int ticks) {
        getRealPlayer().setMaximumAir(ticks);
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return getRealPlayer().getMaximumNoDamageTicks();
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        getRealPlayer().setMaximumNoDamageTicks(ticks);
    }

    @Override
    public double getLastDamage() {
        return getRealPlayer().getLastDamage();
    }

    @Override
    public void setLastDamage(double damage) {
        getRealPlayer().setLastDamage(damage);
    }

    @Override
    public int getNoDamageTicks() {
        return getRealPlayer().getNoDamageTicks();
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        getRealPlayer().setNoDamageTicks(ticks);
    }

    @Override
    public Player getKiller() {
        return getRealPlayer().getKiller();
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect) {
        return getRealPlayer().addPotionEffect(effect);
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        return getRealPlayer().addPotionEffect(effect, force);
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        return getRealPlayer().addPotionEffects(effects);
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        return getRealPlayer().hasPotionEffect(type);
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        return getRealPlayer().getPotionEffect(type);
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        getRealPlayer().removePotionEffect(type);
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return getRealPlayer().getActivePotionEffects();
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        return getRealPlayer().hasLineOfSight(other);
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return getRealPlayer().getRemoveWhenFarAway();
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        getRealPlayer().setRemoveWhenFarAway(remove);
    }

    @Override
    public EntityEquipment getEquipment() {
        return getRealPlayer().getEquipment();
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        getRealPlayer().setCanPickupItems(pickup);
    }

    @Override
    public boolean getCanPickupItems() {
        return getRealPlayer().getCanPickupItems();
    }

    @Override
    public boolean isLeashed() {
        return getRealPlayer().isLeashed();
    }

    @Override
    @Nullable
    public Entity getLeashHolder() throws IllegalStateException {
        return getRealPlayer().getLeashHolder();
    }

    @Override
    public boolean setLeashHolder(Entity holder) {
        return getRealPlayer().setLeashHolder(holder);
    }

    @Override
    public boolean isGliding() {
        return getRealPlayer().isGliding();
    }

    @Override
    public void setGliding(boolean gliding) {
        getRealPlayer().setGliding(gliding);
    }

    @Override
    public boolean isSwimming() {
        return getRealPlayer().isSwimming();
    }

    @Override
    public void setSwimming(boolean swimming) {
        getRealPlayer().setSwimming(swimming);
    }

    @Override
    public boolean isRiptiding() {
        return getRealPlayer().isRiptiding();
    }

    @Override
    public boolean isSleeping() {
        return getRealPlayer().isSleeping();
    }

    @Override
    public void setAI(boolean ai) {
        getRealPlayer().setAI(ai);
    }

    @Override
    public boolean hasAI() {
        return getRealPlayer().hasAI();
    }

    @Override
    public void setCollidable(boolean collidable) {
        getRealPlayer().setCollidable(collidable);
    }

    @Override
    public boolean isCollidable() {
        return getRealPlayer().isCollidable();
    }

    @Override
    public <T> T getMemory(MemoryKey<T> memoryKey) {
        return getRealPlayer().getMemory(memoryKey);
    }

    @Override
    public <T> void setMemory(MemoryKey<T> memoryKey, T memoryValue) {
        getRealPlayer().setMemory(memoryKey,memoryValue);
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        return getRealPlayer().getAttribute(attribute);
    }

    @Override
    public void damage(double amount) {
        getRealPlayer().damage(amount);
    }

    @Override
    public void damage(double amount, Entity source) {
        getRealPlayer().damage(amount, source);
    }

    @Override
    public double getHealth() {
        return getRealPlayer().getHealth();
    }

    @Override
    public void setHealth(double health) {
        getRealPlayer().setHealth(health);;
    }

    @Override
    public double getAbsorptionAmount() {
        return getRealPlayer().getAbsorptionAmount();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        getRealPlayer().setAbsorptionAmount(amount);
    }

    @Override
    @Deprecated
    public double getMaxHealth() {
        return getRealPlayer().getMaxHealth();
    }

    @Override
    @Deprecated
    public void setMaxHealth(double health) {
        getRealPlayer().setMaxHealth(health);
    }

    @Override
    @Deprecated
    public void resetMaxHealth() {
        getRealPlayer().resetMaxHealth();
    }

    @Override
    @Nullable
    public String getCustomName() {
        return getRealPlayer().getCustomName();
    }

    @Override
    public void setCustomName(String name) {
        getRealPlayer().setCustomName(name);
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        getRealPlayer().setMetadata(metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return null;
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return getRealPlayer().hasMetadata(metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        getRealPlayer().removeMetadata(metadataKey, owningPlugin);
    }

    @Override
    public boolean isPermissionSet(String name) {
        return getRealPlayer().isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return getRealPlayer().isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return getRealPlayer().hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return getRealPlayer().hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return getRealPlayer().addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return getRealPlayer().addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return getRealPlayer().addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return getRealPlayer().addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        getRealPlayer().removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        getRealPlayer().recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return getRealPlayer().getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return getRealPlayer().isOp();
    }

    @Override
    public void setOp(boolean value) {
        getRealPlayer().setOp(value);
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return getRealPlayer().getPersistentDataContainer();
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        getRealPlayer().sendPluginMessage(source,channel,message);
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return getRealPlayer().getListeningPluginChannels();
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return getRealPlayer().launchProjectile(projectile);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        return getRealPlayer().launchProjectile(projectile, velocity);
    }

    @Override
    public String toString() {
        return cachedName+" ("+player.toString()+")";
    }
}
