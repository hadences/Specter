package net.hadences.render;

import net.hadences.Specter;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceManager;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class SpecterShaderManager {

//    public static String MOD_ID = "namespace";
//
//    /**
//     * sets the mod namespace
//     * mainly used for shader creation
//     * @see net.hadences.SpecterClient initialize it in the client mod initializer
//     * @param modId the mod namespace
//     */
//    public static void setModNamespace(String modId) {
//        MOD_ID = modId;
//    }
//
//    /**
//     * gets the mod namespace
//     * @return the mod namespace
//     */
//    public static String getModNamespace() {
//        return MOD_ID;
//    }

    /**
     * creates a shader program
     * @param manager the resource manager
     * @param name the name of the shader program
     * @param vertexFormat the vertex format
     * @return the shader program
     */
    @Nullable
    public static ShaderProgram createShaderProgram(ResourceManager manager, String name, VertexFormat vertexFormat) {
        ShaderProgram shaderProgram = null;
        try {
            shaderProgram = new ShaderProgram(manager, name, vertexFormat);
        }catch (Exception e){
            Specter.LOGGER.error("Failed to load custom shader program", e);
        }

        return shaderProgram;
    }
}
