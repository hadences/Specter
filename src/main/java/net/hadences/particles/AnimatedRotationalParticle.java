package net.hadences.particles;

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.mixin.client.particle.ParticleManagerAccessor.SimpleSpriteProviderAccessor;
import net.hadences.particles.behaviors.SpecterParticleBehavior;
import net.hadences.particles.behaviors.SpecterParticleBehaviorRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class AnimatedRotationalParticle extends SpriteRotationalParticle {

    private final SpriteProvider spriteProvider;
    private float targetRed = 0f;
    private float targetGreen = 0f;
    private float targetBlue = 0f;
    private boolean changesColor = false;
    private final boolean repeat;
    private int spriteIndex = 0;
    private int delayTicks = 0;
    private final SpecterParticleBehavior behavior;

    public AnimatedRotationalParticle(ClientWorld world, double x, double y, double z,
                                      double velocityX, double velocityY, double velocityZ,
                                      float rotationYaw, float rotationPitch, float rotationRoll,
                                      float scale, boolean isStatic, float gravityStrength,
                                      SpriteProvider spriteProvider, boolean repeat,
                                      RenderType renderType, int delayTicks, String behaviorIdentifier, int targetEntityID) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, rotationYaw, rotationPitch, rotationRoll, scale, isStatic, renderType);
        this.delayTicks = delayTicks;
        this.spriteProvider = spriteProvider;
        this.repeat = repeat;
        this.velocityMultiplier = 0.91f;
        this.gravityStrength = gravityStrength;
        this.behavior = Objects.requireNonNull(SpecterParticleBehaviorRegistry.getBehavior(Identifier.of(behaviorIdentifier))).clone();
        setSpriteForAge(spriteProvider);

        Entity targetEntity = getTargetEntity(targetEntityID);

        // call init
        if(this.behavior != null){
            this.behavior.init(this, targetEntity);
        }
    }

    public void setColor(int rgbHex) {
        float f = ((rgbHex & 16711680) >> 16) / 255.0f;
        float g = ((rgbHex & 65280) >> 8) / 255.0f;
        float h = ((rgbHex & 255)) / 255.0f;
        this.setColor(f, g, h);
    }

    public void setTargetColor(int rgbHex) {
        this.targetRed = ((rgbHex & 16711680) >> 16) / 255.0f;
        this.targetGreen = ((rgbHex & 65280) >> 8) / 255.0f;
        this.targetBlue = (rgbHex & 255) / 255.0f;
        this.changesColor = true;
    }

    @Override
    public void tick() {
        if(this.delayTicks > 0){
            this.delayTicks--;
            return;
        }

        super.tick();

        if (repeat) {
            setSpriteForRepeat(this.spriteProvider);
        } else {
            setSpriteForAge(this.spriteProvider);
        }

        if (this.age > this.maxAge / 2) {
            int halfLife = this.maxAge / 2;
            int ageOverHalf = this.age - halfLife;
            this.alpha = 1.0f - (ageOverHalf / (float) halfLife);

            if (this.changesColor) {
                this.red += (this.targetRed - this.red) * 0.2f;
                this.green += (this.targetGreen - this.green) * 0.2f;
                this.blue += (this.targetBlue - this.blue) * 0.2f;
            }
        }

        if(this.behavior != null){
            this.behavior.onTick(this);
        }
    }

    @Contract(pure = true)
    private @Nullable Entity getTargetEntity(int entityID) {
        World world = MinecraftClient.getInstance().world;
        if(world == null) return null;
        // fetch entity
        return world.getEntityById(entityID);
    }

    @SuppressWarnings("ALL")
    private void setSpriteForRepeat(SpriteProvider spriteProvider) {
        if (!this.dead) {
            List<Sprite> sprites;

            if (spriteProvider instanceof FabricSpriteProvider) {
                sprites = ((FabricSpriteProvider) spriteProvider).getSprites();
            } else if (spriteProvider instanceof SimpleSpriteProviderAccessor) {
                sprites = ((SimpleSpriteProviderAccessor) spriteProvider).getSprites();
            } else {
                return;
            }

            spriteIndex++;
            if (spriteIndex >= sprites.size()) {
                spriteIndex = 0;
            }

            this.setSprite(sprites.get(spriteIndex));
        }
    }
}
