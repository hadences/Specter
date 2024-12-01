package net.hadences;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.hadences.render.SpecterParticleSheets;
import net.hadences.render.SpecterShaderContext;
import net.hadences.render.SpecterShaderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.Map;

import static net.hadences.particles.types.SpecterParticleTypes.registerParticleFactories;

public class SpecterClient implements ClientModInitializer {
    public static final Identifier specterShaderProgram = Identifier.of(Specter.MOD_ID,"default");
//    public static final Identifier testShaderProgram = Identifier.of(Specter.MOD_ID,"test");

    @Override
    public void onInitializeClient() {
        registerParticleFactories();
        specterShaderInit();
    }

    /**
     * Initializes the specter shader
     * Must be called in onInitializeClient of the main project that this library is used in
     */
    public static void specterShaderInit(){
        // Register the resource reload listener
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new MyShaderReloadListener());

        SpecterParticleSheets.addCustomSheet(specterShaderProgram, SpecterParticleSheets.SPECTER_PARTICLE_SHEET);
//        SpecterParticleSheets.addCustomSheet(testShaderProgram, new ParticleTextureSheet() {
//            public BufferBuilder begin(Tessellator tessellator, TextureManager textureManager) {
//                RenderSystem.depthMask(true);
//                RenderSystem.setShader(() -> SpecterShaderManager.getShaderProgram(SpecterClient.testShaderProgram));
//                RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
//                RenderSystem.enableBlend();
//                RenderSystem.defaultBlendFunc();
//                return tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
//            }
//
//            public String toString() {
//                return "SPECTER_PARTICLE_SHEET";
//            }
//        });
        SpecterParticleSheets.reloadCustomSheets();
        // Load your shader program
        SpecterShaderManager.addShaderProgram(specterShaderProgram, new SpecterShaderContext(specterShaderProgram, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT));
//        SpecterShaderManager.addShaderProgram(testShaderProgram, new SpecterShaderContext(testShaderProgram, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT));
    }

    public static class MyShaderReloadListener implements SimpleSynchronousResourceReloadListener {
        @Override
        public Identifier getFabricId() {
            return Identifier.of(Specter.MOD_ID, "my_shader_reload_listener");
        }

        @Override
        public void reload(ResourceManager manager) {

            Map<Identifier, SpecterShaderContext> shaderPrograms = SpecterShaderManager.getShaderContexts();
            GameRenderer gameRenderer = MinecraftClient.getInstance().gameRenderer;

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

            gameRenderer.loadPrograms(manager);
            SpecterParticleSheets.reloadCustomSheets();
        }
    }
}
