package net.hadences.particles.behaviors.presets;

import net.hadences.particles.behaviors.SpecterParticleBehavior;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class MoveParticleBehavior implements SpecterParticleBehavior {

    PlayerEntity player;

    @Override
    public void init(Particle particle, @Nullable Entity entity) {
        if(entity == null) return;
        if(entity instanceof PlayerEntity p){
            player = p;
        }else{
            return;
        }

        player.sendMessage(Text.literal("called from init!"));
    }

    @Override
    public void onTick(Particle particle) {
        if(player == null) return;
        player.sendMessage(Text.literal("called from tick!"));
    }
}
