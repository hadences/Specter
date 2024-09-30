package net.hadences.particles;

import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;

import java.util.List;

public abstract class SpriteRotationalParticle extends RotationalParticle {

    private Sprite sprite;

    public SpriteRotationalParticle(ClientWorld world, double x, double y, double z,
                                    double velocityX, double velocityY, double velocityZ,
                                    float rotationYaw, float rotationPitch, float rotationRoll,
                                    float scale, boolean isStatic, RenderType renderType) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, scale, isStatic, rotationYaw, rotationPitch, rotationRoll, renderType);
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setSprite(SpriteProvider spriteProvider) {
        this.setSprite(spriteProvider.getSprite(this.random));
    }

    @SuppressWarnings("ALL")
    public void setSpriteForAge(SpriteProvider spriteProvider) {
        if (!this.dead) {
            List<Sprite> sprites = List.of();
            if (spriteProvider instanceof FabricSpriteProviderImpl spriteProviderImpl) {
                sprites = spriteProviderImpl.getSprites();
            }

            int frames = sprites.size();
            if (frames == 0) return;

            float age = this.age;
            float maxAge = this.maxAge;
            int frameIndex = Math.min((int) ((age / maxAge) * frames), frames - 1);
            this.setSprite(sprites.get(frameIndex));
        }
    }

    @Override
    public float getMinU() {
        return this.sprite.getMinU();
    }

    @Override
    public float getMaxU() {
        return this.sprite.getMaxU();
    }

    @Override
    public float getMinV() {
        return this.sprite.getMinV();
    }

    @Override
    public float getMaxV() {
        return this.sprite.getMaxV();
    }
}
