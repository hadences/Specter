package net.hadences.render;

import net.hadences.Specter;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpecterShaderManager {

    private static final Map<Identifier, SpecterShaderContext> specterShaderPrograms = new HashMap<>();
    private static final Map<Identifier, ShaderProgram> specterShaderProgramsLoaded = new HashMap<>();

    public static void addShaderProgram(Identifier identifier, SpecterShaderContext context){
        specterShaderPrograms.put(identifier, context);
    }

    public static Map<Identifier, SpecterShaderContext> getShaderPrograms(){
        return specterShaderPrograms;
    }

    public static ShaderProgram getShaderProgram(Identifier identifier){
        return specterShaderProgramsLoaded.get(identifier);
    }

    public static void removeShaderProgram(Identifier identifier){
        specterShaderProgramsLoaded.remove(identifier);
    }

    /**
     * loads a shader program
     * @param manager the resource manager
     * @param identifier the identifier
     * @param vertexFormat the vertex format
     */
    public static void loadShaderProgram(ResourceManager manager, Identifier identifier, VertexFormat vertexFormat) {
        ShaderProgram shaderProgram = null;
        try {
            shaderProgram = new ShaderProgram(manager, identifier.getPath(), vertexFormat);
        }catch (Exception e){
            Specter.LOGGER.error("Failed to load custom shader program", e);
        }

        if(shaderProgram != null){
            if(specterShaderProgramsLoaded.containsKey(identifier)){
                specterShaderProgramsLoaded.replace(identifier, shaderProgram);
                return;
            }
            specterShaderProgramsLoaded.put(identifier, shaderProgram);
        }
    }
}
