package net.hadences.render;

import net.hadences.Specter;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class SpecterShaderContext implements Cloneable{
    private Identifier identifier;
    private VertexFormat vertexFormat;
    private final HashMap<String, Object> uniforms;

    public SpecterShaderContext(SpecterShaderContext context) {
        this.identifier = context.getIdentifier();
        this.vertexFormat = context.getVertexFormat();
        this.uniforms = new HashMap<>(context.getUniforms());
    }

    public SpecterShaderContext(Identifier identifier, VertexFormat vertexFormat) {
        this.identifier = identifier;
        this.vertexFormat = vertexFormat;
        this.uniforms = new HashMap<>();
    }

    public Identifier getIdentifier() {
        return identifier;

    }

    public VertexFormat getVertexFormat() {
        return vertexFormat;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public void setVertexFormat(VertexFormat vertexFormat) {
        this.vertexFormat = vertexFormat;
    }

    // Add a uniform to the context
    public void setUniform(String name, Object value) {
        uniforms.put(name, value);
    }

    // Retrieve the uniforms for this shader
    public Map<String, Object> getUniforms() {
        return uniforms;
    }

    @Override
    public SpecterShaderContext clone() {
        try {
            return (SpecterShaderContext) super.clone();
        } catch (CloneNotSupportedException e) {
            // Handle the exception, or log it
            Specter.LOGGER.error("Failed to clone SpecterShaderContext", e);
            return null;
        }
    }
}
