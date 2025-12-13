package ru.megantcs.enhancer.platform.render.engine.common.luaLibraries;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;

@LuaExportClass(name = "Player")
public class LuaPlayer
{
    private final PlayerEntity player;

    @LuaExportMethod
    public LuaPlayer(PlayerEntity player) {
        this.player = player;
    }

    @LuaExportMethod
    public String getDisplayName() {
        return player.getDisplayName().getString();
    }

    @LuaExportMethod
    public String getName() {
        return player.getName().getString();
    }

    @LuaExportMethod
    public float getHealth() {
        return player.getHealth();
    }

    @LuaExportMethod
    public float getMaxHealth() {
        return player.getMaxHealth();
    }

    @LuaExportMethod
    public float getHunger() {
        return player.getHungerManager().getFoodLevel();
    }

    @LuaExportMethod
    public float getSaturation() {
        return player.getHungerManager().getSaturationLevel();
    }

    @LuaExportMethod
    public float getExperience() {
        return player.experienceProgress;
    }

    @LuaExportMethod
    public int getLevel() {
        return player.experienceLevel;
    }

    @LuaExportMethod
    public boolean isSneaking() {
        return player.isSneaking();
    }

    @LuaExportMethod
    public boolean isSprinting() {
        return player.isSprinting();
    }

    @LuaExportMethod
    public boolean isFlying() {
        return player.getAbilities().flying;
    }

    @LuaExportMethod
    public boolean isCreative() {
        return player.isCreative();
    }

    @LuaExportMethod
    public boolean isSpectator() {
        return player.isSpectator();
    }

    @LuaExportMethod
    public float getPitch() {
        return player.getPitch();
    }

    @LuaExportMethod
    public float getYaw() {
        return player.getYaw();
    }

    @LuaExportMethod
    public double getX() {
        return player.getX();
    }

    @LuaExportMethod
    public double getY() {
        return player.getY();
    }

    @LuaExportMethod
    public double getZ() {
        return player.getZ();
    }

    @LuaExportMethod
    public double getMotionX() {
        return player.getVelocity().x;
    }

    @LuaExportMethod
    public double getMotionY() {
        return player.getVelocity().y;
    }

    @LuaExportMethod
    public double getMotionZ() {
        return player.getVelocity().z;
    }

    @LuaExportMethod
    public String getDimension() {
        return player.getWorld().getRegistryKey().getValue().toString();
    }

    @LuaExportMethod
    public boolean isOnGround() {
        return player.isOnGround();
    }

    @LuaExportMethod
    public boolean isInWater() {
        return player.isSubmergedInWater();
    }

    @LuaExportMethod
    public boolean isInLava() {
        return player.isInLava();
    }

    @LuaExportMethod
    public float getAir() {
        return player.getAir();
    }

    @LuaExportMethod
    public boolean isOnFire() {
        return player.isOnFire();
    }

    @LuaExportMethod
    public boolean isInvisible() {
        return player.isInvisible();
    }

    @LuaExportMethod
    public boolean isGlowing() {
        return player.isGlowing();
    }

    @LuaExportMethod
    public int getArmor() {
        return player.getArmor();
    }

    @LuaExportMethod
    public float getAttackCooldown() {
        return player.getAttackCooldownProgress(0.0f);
    }

    @LuaExportMethod
    public boolean isUsingItem() {
        return player.isUsingItem();
    }

    @LuaExportMethod
    public String getHeldItem() {
        if (player.getMainHandStack().isEmpty()) {
            return "empty";
        }
        return player.getMainHandStack().getItem().getName().getString();
    }

    @LuaExportMethod
    public boolean isSleeping() {
        return player.isSleeping();
    }

    @LuaExportMethod
    public boolean isDead() {
        return player.isDead();
    }

    @LuaExportMethod
    public long getUuidMost() {
        return player.getUuid().getMostSignificantBits();
    }

    @LuaExportMethod
    public long getUuidLeast() {
        return player.getUuid().getLeastSignificantBits();
    }

    @LuaExportMethod
    public String getUuid() {
        return player.getUuid().toString();
    }

    @LuaExportMethod
    public float getDistanceTo(double x, double y, double z) {
        return (float) player.getPos().distanceTo(new Vec3d(x, y, z));
    }

    @LuaExportMethod
    public float getDistanceToPlayer(LuaPlayer other) {
        return (float) player.getPos().distanceTo(other.player.getPos());
    }

    @LuaExportMethod
    public boolean isHurt() {
        return player.hurtTime > 0;
    }

    @LuaExportMethod
    public int getHurtTime() {
        return player.hurtTime;
    }

    @LuaExportMethod
    public float getAbsorption() {
        return player.getAbsorptionAmount();
    }

    @LuaExportMethod
    public boolean hasPotionEffect(String effect) {
        // Упрощенная проверка эффектов
        return player.getActiveStatusEffects().keySet().stream()
                .anyMatch(potion -> potion.getName().getString().toLowerCase().contains(effect.toLowerCase()));
    }

    @LuaExportMethod
    public boolean isRiding() {
        return player.hasVehicle();
    }

    @LuaExportMethod
    public String getRidingEntity() {
        if (!player.hasVehicle()) return "none";
        return player.getVehicle().getType().getName().getString();
    }

    @LuaExportMethod
    public boolean isWet() {
        return player.isWet();
    }

    @LuaExportMethod
    public int getScore() {
        return player.getScore();
    }

    @LuaExportMethod
    public boolean isAlive() {
        return player.isAlive();
    }

    @LuaExportMethod
    public double getEyeHeight() {
        return player.getEyeY() - player.getY();
    }

    @LuaExportMethod
    public double getEyeX() {
        return player.getEyePos().x;
    }

    @LuaExportMethod
    public double getEyeY() {
        return player.getEyePos().y;
    }

    @LuaExportMethod
    public double getEyeZ() {
        return player.getEyePos().z;
    }

    @LuaExportMethod
    public boolean canSeeSky() {
        BlockPos pos = player.getBlockPos();
        return player.getWorld().isSkyVisible(pos);
    }

    @LuaExportMethod
    public boolean isInRain() {
        return player.getWorld().hasRain(player.getBlockPos());
    }

    @LuaExportMethod
    public String getBiome() {
        return player.getWorld().getBiome(player.getBlockPos()).getKey().get()
                .getValue().toString();
    }

    @LuaExportMethod
    public float getBrightness() {
        return player.getWorld().getLightLevel(player.getBlockPos());
    }

    @LuaExportMethod
    public boolean isFrozen() {
        return player.isFrozen();
    }

    @LuaExportMethod
    public int getFreezeTime() {
        return player.getFrozenTicks();
    }

    @LuaExportMethod
    public float getFallDistance() {
        return player.fallDistance;
    }

    @LuaExportMethod
    public int getFireTicks() {
        return player.getFireTicks();
    }

    @LuaExportMethod
    public boolean isSwimming() {
        return player.isSwimming();
    }

    @LuaExportMethod
    public boolean isCrawling() {
        return player.isCrawling();
    }

    @LuaExportMethod
    public String toString() {
        return "Player{" + getName() + "}";
    }

    @LuaExportMethod
    public boolean equals(LuaPlayer other) {
        return this.player.getUuid().equals(other.player.getUuid());
    }
}