package net.hadences.mixin.client;

import net.hadences.Specter;
import net.hadences.render.SpecterShaderContext;
import net.hadences.render.SpecterShaderManager;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(GameRenderer.class)
public class SpecterGameRendererMixin {
    @Shadow
    @Final
    private Map<String, ShaderProgram> programs;

//    @Inject(method="loadPrograms", at=@At("TAIL"))
//    private void loadSpecterShaders(ResourceFactory factory, CallbackInfo ci){
//        Map<Identifier, SpecterShaderContext> shaderPrograms = SpecterShaderManager.getShaderPrograms();
//
//        Specter.LOGGER.info(shaderPrograms.size() + " shader programs to load");
//
//        for(Identifier identifier : shaderPrograms.keySet()){
//            // Close the old shader if it exists
//            ShaderProgram shaderProgram = SpecterShaderManager.getShaderProgram(identifier);
//            if(shaderProgram == null) continue;
//            programs.put(identifier.toString(), shaderProgram);
//        }
//    }

//    @Inject(method = "loadPrograms", at = @At(value = "INVOKE",
//            target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void loadMyShader(ResourceFactory factory, CallbackInfo ci, List<Pair<ShaderProgram, Consumer<ShaderProgram>>> list2) {
//        Map<Identifier, SpecterShaderContext> shaderPrograms = SpecterShaderManager.getShaderPrograms();
//
//        Specter.LOGGER.info(shaderPrograms.size() + " shader programs to load");
//
//        for(Identifier identifier : shaderPrograms.keySet()){
//            // Close the old shader if it exists
//            ShaderProgram shaderProgram = SpecterShaderManager.getShaderProgram(identifier);
//            programs.put(identifier.toString(), shaderProgram);
//        }

//        try {
//            // Close the old shader if it exists
//            if (SpecterClient.testShaderProgram != null) {
//                SpecterClient.testShaderProgram.close();
//            }
//            Specter.LOGGER.info("Test Loading shader program");
//            // Load your shader program
//            ShaderProgram myShader = new ShaderProgram(factory, "test", VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
//            // Add your shader to the list2 so it will be initialized properly
//            list2.add(Pair.of(myShader, (program) -> {
//                SpecterClient.testShaderProgram = program;
//            }));
//        } catch (IOException e) {
//            Specter.LOGGER.error("Failed to load shader program", e);
//        }
//    }
}

