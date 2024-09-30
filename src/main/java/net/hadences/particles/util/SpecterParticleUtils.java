package net.hadences.particles.util;


import net.hadences.Specter;
import net.hadences.particles.RotationalParticle;
import net.hadences.particles.types.PlaneParticleEffect;
import net.minecraft.server.world.ServerWorld;

/**
 * Main Utility class for rendering particles.
 */
public class SpecterParticleUtils {

    /**
     * Spawn particles based on the PlaneParticle.
     * @see net.hadences.particles.PlaneParticle
     * @param serverWorld the world from the server
     * @param x x position
     * @param y y position
     * @param z z position
     * @param count the number of particles to spawn
     * @param dx x velocity
     * @param dy y velocity
     * @param dz z velocity
     * @param speed the speed of the particle
     * @param yaw the yaw rotation
     * @param pitch the pitch rotation
     * @param roll the roll rotation
     * @param scale the scale of the particle
     * @param maxAge the max age the particle should be alive for
     * @param isStatic removes random velocity applied to particles by default if TRUE
     * @param gravityStrength the gravity on the particle
     * @param color the color of the particle (0xffffff)
     * @param targetColor the color to lerp to over time (default 0xffffff)
     * @param repeat setting this to true will override the default frame sequence over time and play
     *               the frames per tick (by default, the given frames are played evenly over maxAge)
     * @param renderType the render type of the particle
     */
    public static void spawnPlaneParticle(ServerWorld serverWorld,
                                          double x, double y, double z, int count, double dx, double dy, double dz, double speed,
                                          float yaw, float pitch, float roll, float scale, int maxAge, boolean isStatic,
                                          float gravityStrength, int color, int targetColor, boolean repeat, RotationalParticle.RenderType renderType,
                                          String behaviorIdentifier, int targetEntityID) {

        // Create the PlaneParticleType instance
        PlaneParticleEffect particleEffect = new PlaneParticleEffect(
                yaw,
                pitch,
                roll,
                scale,
                isStatic,
                gravityStrength,
                maxAge,
                color,
                targetColor,
                repeat,
                renderType.ordinal(),
                behaviorIdentifier,
                targetEntityID
        );

        // Spawn the particles in the server world
        serverWorld.spawnParticles(particleEffect, x, y, z, count, dx, dy, dz, speed);
    }

    public static void spawnPlaneParticle(ServerWorld serverWorld,
                                          double x, double y, double z, int count, double dx, double dy, double dz, double speed,
                                          float yaw, float pitch, float roll, float scale, int maxAge, boolean isStatic,
                                          float gravityStrength, int color, int targetColor, boolean repeat, RotationalParticle.RenderType renderType) {

        spawnPlaneParticle(serverWorld, x, y, z, count, dx, dy, dz, speed, yaw, pitch, roll, scale, maxAge, isStatic, gravityStrength, color, targetColor, repeat, renderType, Specter.NONE_BEHAVIOR, -1);
    }
}

