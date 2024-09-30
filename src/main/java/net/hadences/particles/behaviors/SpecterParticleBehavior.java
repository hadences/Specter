package net.hadences.particles.behaviors;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface SpecterParticleBehavior {
    void init(Particle particle, @Nullable Entity entity);

    void onTick(Particle particle);
}
