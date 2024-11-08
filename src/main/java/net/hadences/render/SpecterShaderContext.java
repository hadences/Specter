package net.hadences.render;

import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;

public class SpecterShaderContext {
    private Identifier identifier;
    private VertexFormat vertexFormat;

    public SpecterShaderContext(Identifier identifier, VertexFormat vertexFormat) {
        this.identifier = identifier;
        this.vertexFormat = vertexFormat;
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
}
