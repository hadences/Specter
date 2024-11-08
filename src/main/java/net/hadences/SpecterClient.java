package net.hadences;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.hadences.render.SpecterParticleSheets;
import net.hadences.render.SpecterShaderContext;
import net.hadences.render.SpecterShaderManager;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.Map;

import static net.hadences.particles.types.SpecterParticleTypes.registerParticleFactories;

public class SpecterClient implements ClientModInitializer {
    public static final Identifier defaultShaderProgram = Identifier.of(Specter.MOD_ID,"default");

    @Override
    public void onInitializeClient() {
        registerParticleFactories();
        // Register the resource reload listener
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new MyShaderReloadListener());
        SpecterParticleSheets.init();

        // Load your shader program
        SpecterShaderManager.addShaderProgram(defaultShaderProgram, new SpecterShaderContext(defaultShaderProgram, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT));
    }

    public static ShaderProgram getDefaultShaderProgram() {
        return SpecterShaderManager.getShaderProgram(defaultShaderProgram);
    }

    private static class MyShaderReloadListener implements SimpleSynchronousResourceReloadListener {
        @Override
        public Identifier getFabricId() {
            return Identifier.of(Specter.MOD_ID, "my_shader_reload_listener");
        }

        @Override
        public void reload(ResourceManager manager) {

            Map<Identifier, SpecterShaderContext> shaderPrograms = SpecterShaderManager.getShaderPrograms();

            Specter.LOGGER.info(shaderPrograms.size() + " shader programs to load");

            for(Identifier identifier : shaderPrograms.keySet()){
                SpecterShaderContext context = shaderPrograms.get(identifier);

                // Close the old shader if it exists
                ShaderProgram shaderProgram = SpecterShaderManager.getShaderProgram(identifier);
                if (shaderProgram != null) {
                    Specter.LOGGER.info("Closing old shader program");
                    shaderProgram.close();
                    SpecterShaderManager.removeShaderProgram(identifier);
                }

                SpecterShaderManager.loadShaderProgram(manager, identifier, context.getVertexFormat());
                ShaderProgram newShaderProgram = SpecterShaderManager.getShaderProgram(identifier);
                if(newShaderProgram == null){
                    Specter.LOGGER.error("Failed to load custom shader program");
                    return;
                }
                Specter.LOGGER.info("Shader program loaded successfully: {}", newShaderProgram.getName());
            }

            // Close the old shader if it exists
//            if (SpecterClient.testShaderProgram != null) {
//                Specter.LOGGER.info("Closing old shader program");
//                SpecterClient.testShaderProgram.close();
//                SpecterClient.testShaderProgram = null;
//            }
//
//            Specter.LOGGER.info("Loading custom shader program");
//            // Load your shader program
//            SpecterClient.testShaderProgram = SpecterShaderManager.loadShaderProgram(manager, "test", VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
//            if(SpecterClient.testShaderProgram == null){
//                Specter.LOGGER.error("Failed to load custom shader program");
//                return;
//            }
//            Specter.LOGGER.info("Shader program loaded successfully: {}", SpecterClient.testShaderProgram.getName());


            SpecterParticleSheets.init();
        }
    }
}
