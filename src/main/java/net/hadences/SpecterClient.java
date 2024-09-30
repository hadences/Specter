package net.hadences;

import net.fabricmc.api.ClientModInitializer;

import static net.hadences.particles.types.SpecterParticleTypes.registerParticleFactories;

public class SpecterClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerParticleFactories();
    }
}
