package net.hadences.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.mixin.client.particle.ParticleManagerAccessor.SimpleSpriteProviderAccessor;
import net.hadences.Specter;
import net.hadences.SpecterClient;
import net.hadences.particles.behaviors.SpecterParticleBehavior;
import net.hadences.particles.behaviors.SpecterParticleBehaviorRegistry;
import net.hadences.render.SpecterParticleSheets;
import net.hadences.render.SpecterShaderContext;
import net.hadences.render.SpecterShaderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

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
    private final Identifier shaderIdentifier;

    public AnimatedRotationalParticle(ClientWorld world, double x, double y, double z,
                                      double velocityX, double velocityY, double velocityZ,
                                      float rotationYaw, float rotationPitch, float rotationRoll,
                                      float scale, boolean isStatic, float gravityStrength,
                                      SpriteProvider spriteProvider, boolean repeat,
                                      RenderType renderType, int delayTicks, String behaviorIdentifier, int targetEntityID,
                                      Identifier shaderIdentifier) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, rotationYaw, rotationPitch, rotationRoll, scale, isStatic, renderType);
        this.delayTicks = delayTicks;
        this.spriteProvider = spriteProvider;
        this.repeat = repeat;
        this.velocityMultiplier = 0.91f;
        this.gravityStrength = gravityStrength;
        this.behavior = Objects.requireNonNull(SpecterParticleBehaviorRegistry.getBehavior(Identifier.of(behaviorIdentifier))).clone();
        this.shaderIdentifier = shaderIdentifier;

        setSpriteForAge(spriteProvider);

        Entity targetEntity = getTargetEntity(targetEntityID);

        // call init
        if(this.behavior != null){
            this.behavior.init(this, targetEntity);
        }
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (camera == null || vertexConsumer == null) return;

        var cameraPosition = camera.getPos();
        float particlePosX = (float) (MathHelper.lerp(tickDelta, prevPosX, x) - cameraPosition.x);
        float particlePosY = (float) (MathHelper.lerp(tickDelta, prevPosY, y) - cameraPosition.y);
        float particlePosZ = (float) (MathHelper.lerp(tickDelta, prevPosZ, z) - cameraPosition.z);

        float particleYaw = MathHelper.lerp(tickDelta, prevYaw, yaw);
        float particlePitch = MathHelper.lerp(tickDelta, prevPitch, pitch);
        float particleRoll = MathHelper.lerp(tickDelta, prevRoll, roll);

        this.rotation.identity();

        if (billboard) {
            getRotator().setRotation(this.rotation, camera, tickDelta);
            if (this.angle != 0.0f) {
                rotation.rotateZ(MathHelper.lerp(tickDelta, this.prevAngle, this.angle));
            }
        } else {
            Quaternionf quaternionf = new Quaternionf();
            quaternionf.identity();
            quaternionf.rotateY((float) Math.toRadians(particleYaw));
            quaternionf.rotateX((float) Math.toRadians(particlePitch));
            quaternionf.rotateZ((float) Math.toRadians(particleRoll));
            this.rotation.mul(quaternionf);
        }

        Vector3f[] particleCorners = {
                new Vector3f(-1.0f, -1.0f, 0.0f),
                new Vector3f(-1.0f, 1.0f, 0.0f),
                new Vector3f(1.0f, 1.0f, 0.0f),
                new Vector3f(1.0f, -1.0f, 0.0f)
        };

        float particleSize = getSize(tickDelta);

        for (Vector3f corner : particleCorners) {
            corner.rotate(this.rotation);
            corner.mul(particleSize);
            corner.add(particlePosX, particlePosY, particlePosZ);
        }

        float minU = getMinU();
        float maxU = getMaxU();
        float minV = getMinV();
        float maxV = getMaxV();
        int brightness = getBrightness(tickDelta);

        RenderSystem.setShader(() -> {
            ShaderProgram shaderProgram = SpecterShaderManager.getShaderProgram(shaderIdentifier);
            if (shaderProgram == null) {
                Specter.LOGGER.warn("Custom shader program is null, using default particle shader.");
                return GameRenderer.getParticleProgram();
            }
            return shaderProgram;
        });
        renderQuad(vertexConsumer, particleCorners, minU, maxU, minV, maxV, brightness);
    }

    @Override
    public ParticleTextureSheet getType() {
        return SpecterParticleSheets.SPECTER_PARTICLE_SHEET;
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

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
