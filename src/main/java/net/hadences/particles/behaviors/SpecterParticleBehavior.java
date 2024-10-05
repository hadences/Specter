package net.hadences.particles.behaviors;

import net.hadences.particles.AnimatedRotationalParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface SpecterParticleBehavior {
    void init(Particle particle, @Nullable Entity entity);

    void onTick(Particle particle);
}
