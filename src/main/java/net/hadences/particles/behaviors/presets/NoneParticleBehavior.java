package net.hadences.particles.behaviors.presets;

import net.hadences.particles.behaviors.SpecterParticleBehavior;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class NoneParticleBehavior implements SpecterParticleBehavior {

    @Override
    public void init(Particle particle, @Nullable Entity entity) {
    }

    @Override
    public void onTick(Particle particle) {
    }
}
