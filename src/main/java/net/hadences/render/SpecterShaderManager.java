package net.hadences.render;

import net.hadences.Specter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class SpecterShaderManager {

    private static final Map<Identifier, SpecterShaderContext> specterShaderContext = new HashMap<>();
    private static final Map<Identifier, ShaderProgram> specterShaderProgramsLoaded = new HashMap<>();

    public static void addShaderProgram(Identifier identifier, SpecterShaderContext context){
        specterShaderContext.put(identifier, context);
    }

    public static Map<Identifier, SpecterShaderContext> getShaderContexts(){
        return specterShaderContext;
    }

    public static ShaderProgram getShaderProgram(Identifier identifier){
        return specterShaderProgramsLoaded.get(identifier);
    }

    public static void removeShaderProgram(Identifier identifier){
        specterShaderProgramsLoaded.remove(identifier);
    }

    public static ShaderProgram createNewShaderProgram(Identifier shaderIdentifier, VertexFormat format) {
        ShaderProgram shaderProgram = loadShaderProgramDirectly(shaderIdentifier, format);
        if (shaderProgram == null) {
            Specter.LOGGER.error("Failed to create shader program for {}", shaderIdentifier);
        }
        return shaderProgram;
    }

    private static ShaderProgram loadShaderProgramDirectly(Identifier shaderIdentifier, VertexFormat format) {
        // Load a new shader directly from resources with the given identifier and format
        ShaderProgram shaderProgram = null;
        try {
            shaderProgram = new ShaderProgram(MinecraftClient.getInstance().getResourceManager(), shaderIdentifier.toString(), format);
        } catch (Exception e) {
            Specter.LOGGER.error("Failed to load custom shader program", e);
        }

        return shaderProgram;
    }

    /**
     * loads a shader program
     * @param factory the resource manager
     * @param identifier the identifier
     * @param vertexFormat the vertex format
     */
    public static void loadShaderProgram(ResourceFactory factory, Identifier identifier, VertexFormat vertexFormat) {
        ShaderProgram shaderProgram = null;
        try {
            shaderProgram = new ShaderProgram(factory, identifier.toString(), vertexFormat);
        }catch (Exception e){
            Specter.LOGGER.error("Failed to load custom shader program", e);
        }

        if(shaderProgram != null){
            Specter.LOGGER.info("Loaded shader program: {}", shaderProgram.getName());
            if(specterShaderProgramsLoaded.containsKey(identifier)){
                specterShaderProgramsLoaded.replace(identifier, shaderProgram);
                return;
            }
            specterShaderProgramsLoaded.put(identifier, shaderProgram);
        }
    }
}
