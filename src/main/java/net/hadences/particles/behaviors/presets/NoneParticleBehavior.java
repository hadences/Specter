package net.hadences.particles.behaviors.presets;

import net.hadences.particles.behaviors.SpecterParticleBehavior;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class NoneParticleBehavior implements SpecterParticleBehavior {

    @Override
    public void init(Particle particle, @Nullable Entity entity) {
    }

    @Override
    public void onTick(Particle particle) {
    }

    @Override
    public SpecterParticleBehavior clone() {
        try {
            return (NoneParticleBehavior) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
