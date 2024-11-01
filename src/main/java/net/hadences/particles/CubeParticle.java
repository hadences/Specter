package net.hadences.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hadences.particles.types.PlaneParticleEffect;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CubeParticle extends AnimatedRotationalParticle {

    public CubeParticle(ClientWorld world, double x, double y, double z,
                        double velocityX, double velocityY, double velocityZ,
                        float rotationYaw, float rotationPitch, float rotationRoll,
                        float scale, boolean isStatic, float gravityStrength,
                        SpriteProvider spriteProvider, int maxAge, int color,
                        int targetColor, boolean repeat, RenderType renderType, int delayTicks,
                        String behaviorIdentifier, int targetEntityID) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, rotationYaw, rotationPitch, rotationRoll,
                scale, isStatic, gravityStrength, spriteProvider, repeat, renderType,  delayTicks, behaviorIdentifier, targetEntityID);

        setColor(color);
        setTargetColor(targetColor);
        setSpriteForAge(spriteProvider);
        this.maxAge = maxAge;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (camera == null || vertexConsumer == null) return;

        // Compute particle positions and rotations
        var cameraPosition = camera.getPos();
        float particlePosX = (float) (MathHelper.lerp(tickDelta, prevPosX, x) - cameraPosition.x);
        float particlePosY = (float) (MathHelper.lerp(tickDelta, prevPosY, y) - cameraPosition.y);
        float particlePosZ = (float) (MathHelper.lerp(tickDelta, prevPosZ, z) - cameraPosition.z);

        float particleYaw = MathHelper.lerp(tickDelta, prevYaw, yaw);
        float particlePitch = MathHelper.lerp(tickDelta, prevPitch, pitch);
        float particleRoll = MathHelper.lerp(tickDelta, prevRoll, roll);

        // Set up the overall rotation matrix
        this.rotation.identity();
        Quaternionf quaternionf = new Quaternionf();
        quaternionf.identity();
        quaternionf.rotateY((float) Math.toRadians(particleYaw));
        quaternionf.rotateX((float) Math.toRadians(particlePitch));
        quaternionf.rotateZ((float) Math.toRadians(particleRoll));
        this.rotation.mul(quaternionf);

        float particleSize = getSize(tickDelta);
        float minU = getMinU();
        float maxU = getMaxU();
        float minV = getMinV();
        float maxV = getMaxV();
        int brightness = getBrightness(tickDelta);

        Vector3f[] cubeVertices = getCubeVertices(particleSize);

        // Define faces (each face consists of four vertex indices)
        int[][] faces = new int[][] {
                {1, 2, 6, 5}, // Front face
                {0, 3, 7, 4}, // Back face
                {4, 0, 1, 5}, // Bottom face
                {7, 3, 2, 6}, // Top face
                {0, 3, 2, 1}, // Left face
                {5, 6, 7, 4}  // Right face
        };

        // For each face
        for (int[] face : faces) {
            Vector3f[] faceCorners = new Vector3f[4];
            for (int i = 0; i < 4; i++) {
                faceCorners[i] = new Vector3f(cubeVertices[face[i]]);
                // Apply rotation
                faceCorners[i].rotate(rotation);
                // Translate to particle position
                faceCorners[i].add(particlePosX, particlePosY, particlePosZ);
            }
            // Render the face
            renderQuad(vertexConsumer, faceCorners, minU, maxU, minV, maxV, brightness);
        }

    }

    private Vector3f[] getCubeVertices(float particleSize) {
        float s = particleSize / 2.0f;
        return new Vector3f[] {
                new Vector3f(-s, -s, -s), // 0
                new Vector3f(-s, -s,  s), // 1
                new Vector3f(-s,  s,  s), // 2
                new Vector3f(-s,  s, -s), // 3
                new Vector3f( s, -s, -s), // 4
                new Vector3f( s, -s,  s), // 5
                new Vector3f( s,  s,  s), // 6
                new Vector3f( s,  s, -s), // 7
        };
    }

    /**
     * generates a quad with the given corners and renders it.
     * @return the generated quad
     */
    private Vector3f[] getQuadCorners() {
        return new Vector3f[]{
                new Vector3f(-1.0f, -1.0f, 0.0f),
                new Vector3f(-1.0f, 1.0f, 0.0f),
                new Vector3f(1.0f, 1.0f, 0.0f),
                new Vector3f(1.0f, -1.0f, 0.0f)
        };
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
            int delayTicks = planeParticleEffect != null ? planeParticleEffect.getDelayTicks() : 0;
            String behaviorIdentifier = planeParticleEffect != null ? planeParticleEffect.getBehaviorIdentifier() : "";
            int targetEntityIdentifier = planeParticleEffect != null ? planeParticleEffect.getTargetEntityIdentifier() : -1;

            return new CubeParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ,
                    yaw, pitch, roll, scale, isStatic, gravityStrength,
                    spriteProvider, maxAge, color, targetColor, repeat, renderType, delayTicks, behaviorIdentifier, targetEntityIdentifier);
        }
    }
}

