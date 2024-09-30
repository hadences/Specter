package net.hadences.particles;

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.mixin.client.particle.ParticleManagerAccessor.SimpleSpriteProviderAccessor;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;

import java.util.List;

public class AnimatedRotationalParticle extends SpriteRotationalParticle {

    private final SpriteProvider spriteProvider;
    private float targetRed = 0f;
    private float targetGreen = 0f;
    private float targetBlue = 0f;
    private boolean changesColor = false;
    private final boolean repeat;
    private int spriteIndex = 0;

    public AnimatedRotationalParticle(ClientWorld world, double x, double y, double z,
                                      double velocityX, double velocityY, double velocityZ,
                                      float rotationYaw, float rotationPitch, float rotationRoll,
                                      float scale, boolean isStatic, float gravityStrength,
                                      SpriteProvider spriteProvider, boolean repeat,
                                      RenderType renderType) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, rotationYaw, rotationPitch, rotationRoll, scale, isStatic, renderType);
        this.spriteProvider = spriteProvider;
        this.repeat = repeat;
        this.velocityMultiplier = 0.91f;
        this.gravityStrength = gravityStrength;
        setSpriteForAge(spriteProvider);
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
