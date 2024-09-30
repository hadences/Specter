package net.hadences.particles;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class RotationalParticle extends Particle {

    public enum RenderType {
        BILLBOARD,
        FREE
    }

    private float scale;
    private final Quaternionf rotation;
    private float prevYaw;
    private float prevPitch;
    private float prevRoll;
    private final boolean billboard;
    private float yaw;
    private float pitch;
    private float roll;

    public RotationalParticle(ClientWorld world, double x, double y, double z,
                              double velocityX, double velocityY, double velocityZ,
                              float scale, boolean isStatic, float yaw, float pitch, float roll, RenderType renderType) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.scale = scale;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.rotation = new Quaternionf();
        if (isStatic) {
            this.setVelocity(0.0, 0.0, 0.0);
        }
        this.prevYaw = yaw;
        this.prevPitch = pitch;
        this.prevRoll = roll;
        this.billboard = renderType == RenderType.BILLBOARD;
        this.maxAge = 60;
    }

    protected RotationalParticle(ClientWorld world, double x, double y, double z,
                                 float scale, boolean isStatic, float yaw, float pitch, float roll) {
        this(world, x, y, z, 0.0, 0.0, 0.0, scale, isStatic, yaw, pitch, roll, RenderType.FREE);
    }

    @Override
    public void tick() {
        super.tick();
        prevYaw = yaw;
        prevPitch = pitch;
        prevRoll = roll;
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
            this.rotation.rotateY((float) Math.toRadians(particleYaw));
            this.rotation.rotateX((float) Math.toRadians(particlePitch));
            this.rotation.rotateZ((float) Math.toRadians(particleRoll));
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

        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        renderQuad(vertexConsumer, particleCorners, minU, maxU, minV, maxV, brightness);
        RenderSystem.defaultBlendFunc();
    }

    private void renderQuad(VertexConsumer vertexConsumer, Vector3f[] particleCorners, float minU, float maxU, float minV, float maxV, int brightness) {
        float particleColorR = red;
        float particleColorG = green;
        float particleColorB = blue;

        vertexConsumer.vertex(particleCorners[0].x(), particleCorners[0].y(), particleCorners[0].z())
                .texture(maxU, maxV).color(particleColorR, particleColorG, particleColorB, 1.0f).light(brightness).next();
        vertexConsumer.vertex(particleCorners[1].x(), particleCorners[1].y(), particleCorners[1].z())
                .texture(maxU, minV).color(particleColorR, particleColorG, particleColorB, alpha).light(brightness).next();
        vertexConsumer.vertex(particleCorners[2].x(), particleCorners[2].y(), particleCorners[2].z())
                .texture(minU, minV).color(particleColorR, particleColorG, particleColorB, alpha).light(brightness).next();
        vertexConsumer.vertex(particleCorners[3].x(), particleCorners[3].y(), particleCorners[3].z())
                .texture(minU, maxV).color(particleColorR, particleColorG, particleColorB, alpha).light(brightness).next();

        vertexConsumer.vertex(particleCorners[3].x(), particleCorners[3].y(), particleCorners[3].z())
                .texture(minU, maxV).color(particleColorR, particleColorG, particleColorB, alpha).light(brightness).next();
        vertexConsumer.vertex(particleCorners[2].x(), particleCorners[2].y(), particleCorners[2].z())
                .texture(minU, minV).color(particleColorR, particleColorG, particleColorB, alpha).light(brightness).next();
        vertexConsumer.vertex(particleCorners[1].x(), particleCorners[1].y(), particleCorners[1].z())
                .texture(maxU, minV).color(particleColorR, particleColorG, particleColorB, alpha).light(brightness).next();
        vertexConsumer.vertex(particleCorners[0].x(), particleCorners[0].y(), particleCorners[0].z())
                .texture(maxU, maxV).color(particleColorR, particleColorG, particleColorB, 1.0f).light(brightness).next();
    }

    public int getAge() {
        return this.age;
    }

    public void setPosition(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return this.x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return this.y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getZ() {
        return this.z;
    }

    public void setRotation(float yaw, float pitch, float roll) {
        setRotationYaw(yaw);
        setRotationPitch(pitch);
        setRotationRoll(roll);
    }

    public void setRotationYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRotationYaw() {
        return this.yaw;
    }

    public void setRotationPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRotationPitch() {
        return this.pitch;
    }

    public void setRotationRoll(float roll) {
        this.roll = roll;
    }

    public float getRotationRoll() {
        return this.roll;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public Particle scale(float scale) {
        this.scale *= scale;
        return super.scale(scale);
    }

    public float getSize(float tickDelta) {
        return this.scale;
    }

    public BillboardParticle.Rotator getRotator() {
        return BillboardParticle.Rotator.ALL_AXIS;
    }

    protected abstract float getMinU();

    protected abstract float getMaxU();

    protected abstract float getMinV();

    protected abstract float getMaxV();

    @Override
    public int getBrightness(float tint) {
        return 15728880;
    }
}