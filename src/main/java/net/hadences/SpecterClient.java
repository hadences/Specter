package net.hadences;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.hadences.particles.PlaneParticle;
import net.hadences.particles.types.SpecterParticleTypes;

public class SpecterClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerParticleFactories();
    }

    private void registerParticleFactories(){
        Specter.LOGGER.info("registering particle factories!");

        ParticleFactoryRegistry.getInstance().register(SpecterParticleTypes.PLANE, PlaneParticle.Factory::new);
    }
}
