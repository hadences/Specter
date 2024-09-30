package net.hadences.particles.types;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.hadences.Specter;
import net.hadences.particles.PlaneParticle;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class SpecterParticleTypes {
    public static final ParticleType<PlaneParticleEffect> PLANE;

    public static void init () {
    }

    private static <T extends ParticleEffect> ParticleType<T> register(Identifier identifier, boolean alwaysShow, ParticleEffect.Factory<T> factory, final Function<ParticleType<T>, Codec<T>> codecGetter) {
        return Registry.register(Registries.PARTICLE_TYPE, identifier, new ParticleType<T>(alwaysShow, factory) {
            public Codec<T> getCodec() {
                return codecGetter.apply(this);
            }
        });
    }

    static {
        PLANE = register(new Identifier(Specter.MOD_ID, "plane"), true, PlaneParticleEffect.FACTORY, (type) -> PlaneParticleEffect.CODEC);
    }

    @Environment(EnvType.CLIENT)
    public static void registerParticleFactories(){
        Specter.LOGGER.info("registering particle factories!");

        ParticleFactoryRegistry.getInstance().register(SpecterParticleTypes.PLANE, PlaneParticle.Factory::new);
    }

}
