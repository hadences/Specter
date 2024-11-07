package net.hadences;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.hadences.render.SpecterParticleSheets;
import net.hadences.render.SpecterShaderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static net.hadences.particles.types.SpecterParticleTypes.registerParticleFactories;

public class SpecterClient implements ClientModInitializer {
    public static ShaderProgram testShaderProgram;

    @Override
    public void onInitializeClient() {
        registerParticleFactories();

        // Register the resource reload listener
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new MyShaderReloadListener());
        SpecterParticleSheets.init();
    }

    public static ShaderProgram getTestShaderProgram() {
        return testShaderProgram;
    }

    private static class MyShaderReloadListener implements SimpleSynchronousResourceReloadListener {
        @Override
        public Identifier getFabricId() {
            return Identifier.of(Specter.MOD_ID, "my_shader_reload_listener");
        }

        @Override
        public void reload(ResourceManager manager) {

            // Close the old shader if it exists
            if (SpecterClient.testShaderProgram != null) {
                Specter.LOGGER.info("Closing old shader program");
                SpecterClient.testShaderProgram.close();
                SpecterClient.testShaderProgram = null;
            }

            Specter.LOGGER.info("Loading custom shader program");
            // Load your shader program
            SpecterClient.testShaderProgram = SpecterShaderManager.createShaderProgram(manager, "test", VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
            if(SpecterClient.testShaderProgram == null){
                Specter.LOGGER.error("Failed to load custom shader program");
                return;
            }
            Specter.LOGGER.info("Shader program loaded successfully: {}", SpecterClient.testShaderProgram.getName());


            SpecterParticleSheets.init();
        }
    }
}
