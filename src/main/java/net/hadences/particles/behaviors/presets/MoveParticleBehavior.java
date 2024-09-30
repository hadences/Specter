package net.hadences.particles.behaviors.presets;

import net.hadences.particles.behaviors.SpecterParticleBehavior;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class MoveParticleBehavior implements SpecterParticleBehavior {

    PlayerEntity player;

    @Override
    public void init(Particle particle, @Nullable Entity entity) {
        if(entity == null) return;
        if(entity instanceof PlayerEntity p){
            player = p;
        }
    }

    @Override
    public void onTick(Particle particle) {
        if(player == null) return;

        Vec3d spawnPos = player.getEyePos().add(player.getRotationVector());

        particle.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
    }
}
