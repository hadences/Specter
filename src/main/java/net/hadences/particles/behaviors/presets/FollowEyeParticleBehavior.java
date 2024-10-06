package net.hadences.particles.behaviors.presets;

import net.hadences.particles.AnimatedRotationalParticle;
import net.hadences.particles.behaviors.SpecterParticleBehavior;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class FollowEyeParticleBehavior implements SpecterParticleBehavior {

    PlayerEntity player;
    double distance = 0.0;

    @Override
    public void init(Particle particle, @Nullable Entity entity) {
        if(entity == null) return;
        if(entity instanceof PlayerEntity p){
            player = p;
        }

        if(particle instanceof AnimatedRotationalParticle rotationalParticle) {
            distance = rotationalParticle.getPos().distanceTo(player.getPos());
        }
    }

    @Override
    public void onTick(Particle particle) {
        if(player == null) return;

        Vec3d normalizedRotation = player.getRotationVector().normalize();
        Vec3d spawnPos = player.getEyePos().add(normalizedRotation.multiply(distance));

        particle.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
    }

    @Override
    public SpecterParticleBehavior clone() {
        try {
            return (FollowEyeParticleBehavior) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
