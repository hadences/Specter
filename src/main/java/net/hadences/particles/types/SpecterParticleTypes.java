package net.hadences.particles.types;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.hadences.Specter;
import net.hadences.particles.PlaneParticle;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class SpecterParticleTypes {
    public static final ParticleType<PlaneParticleEffect> SPECTER_TEST;


    public static void init () {
    }

    public static <T extends ParticleEffect> ParticleType<T> register(Identifier identifier, boolean alwaysShow, final Function<ParticleType<T>, MapCodec<T>> codecGetter, final Function<ParticleType<T>, PacketCodec<? super RegistryByteBuf, T>> packetCodecGetter) {
        return Registry.register(Registries.PARTICLE_TYPE, identifier, new ParticleType<T>(alwaysShow) {
            public MapCodec<T> getCodec() {
                return codecGetter.apply(this);
            }

            public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
                return packetCodecGetter.apply(this);
            }
        });
    }

    static {
        SPECTER_TEST = register(Identifier.of(Specter.MOD_ID, "specter_test"), true,
                (type) -> TestParticleEffect.getCodec(),
                (type) -> TestParticleEffect.getPacketCodec());
    }

    @Environment(EnvType.CLIENT)
    public static void registerParticleFactories(){
        Specter.LOGGER.info("Specter: registering particle factories!");
        ParticleFactoryRegistry.getInstance().register(SpecterParticleTypes.SPECTER_TEST, PlaneParticle.Factory::new);
    }

}
