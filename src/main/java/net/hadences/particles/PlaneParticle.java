package net.hadences.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hadences.particles.types.PlaneParticleEffect;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public class PlaneParticle extends AnimatedRotationalParticle {

    public PlaneParticle(ClientWorld world, double x, double y, double z,
                         double velocityX, double velocityY, double velocityZ,
                         float rotationYaw, float rotationPitch, float rotationRoll,
                         float scale, boolean isStatic, float gravityStrength,
                         SpriteProvider spriteProvider, int maxAge, int color,
                         int targetColor, boolean repeat, RenderType renderType,
                         String behaviorIdentifier, int targetEntityID) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, rotationYaw, rotationPitch, rotationRoll,
                scale, isStatic, gravityStrength, spriteProvider, repeat, renderType, behaviorIdentifier, targetEntityID);

        setColor(color);
        setTargetColor(targetColor);
        setSpriteForAge(spriteProvider);
        this.maxAge = maxAge;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory<T extends PlaneParticleEffect> implements ParticleFactory<T> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(T planeParticleEffect, ClientWorld clientWorld,
                                       double x, double y, double z, double velocityX, double velocityY, double velocityZ) {

            float yaw = planeParticleEffect != null ? planeParticleEffect.getYaw() : 0.0f;
            float pitch = planeParticleEffect != null ? planeParticleEffect.getPitch() : 0.0f;
            float roll = planeParticleEffect != null ? planeParticleEffect.getRoll() : 0.0f;
            float scale = planeParticleEffect != null ? planeParticleEffect.getScale() : 1.0f;
            boolean isStatic = planeParticleEffect == null || planeParticleEffect.isStatic();
            float gravityStrength = planeParticleEffect != null ? planeParticleEffect.getGravityStrength() : 0.0f;
            int maxAge = planeParticleEffect != null ? planeParticleEffect.getMaxAge() : 20;
            int color = planeParticleEffect != null ? planeParticleEffect.getColor() : 0xffffff;
            int targetColor = planeParticleEffect != null ? planeParticleEffect.getTargetColor() : 0xffffff;
            boolean repeat = planeParticleEffect != null && planeParticleEffect.isRepeat();
            RenderType renderType = RenderType.values()[planeParticleEffect != null ? planeParticleEffect.getRenderTypeOrdinal() : 0];
            String behaviorIdentifier = planeParticleEffect != null ? planeParticleEffect.getBehaviorIdentifier() : "";
            int targetEntityIdentifier = planeParticleEffect != null ? planeParticleEffect.getTargetEntityIdentifier() : -1;

            return new PlaneParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ,
                    yaw, pitch, roll, scale, isStatic, gravityStrength,
                    spriteProvider, maxAge, color, targetColor, repeat, renderType, behaviorIdentifier, targetEntityIdentifier);
        }
    }
}

