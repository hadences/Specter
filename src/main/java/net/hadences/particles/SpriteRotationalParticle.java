package net.hadences.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.hadences.SpecterClient;
import net.hadences.render.SpecterParticleSheets;
import net.hadences.render.SpecterRenderLayer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

import java.util.List;
import java.util.function.Supplier;

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
//            this.rotation.rotateY((float) Math.toRadians(particleYaw));
//            this.rotation.rotateX((float) Math.toRadians(particlePitch));
//            this.rotation.rotateZ((float) Math.toRadians(particleRoll));
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

//        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        renderQuad(vertexConsumer, particleCorners, minU, maxU, minV, maxV, brightness);
//        RenderSystem.defaultBlendFunc();
    }

    @Override
    public ParticleTextureSheet getType() {
//        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
        return SpecterParticleSheets.SPECTER_PARTICLE_SHEET;
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
