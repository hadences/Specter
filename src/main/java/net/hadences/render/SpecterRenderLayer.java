package net.hadences.render;

import net.hadences.SpecterClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class SpecterRenderLayer extends RenderLayer {

    public static final RenderLayer SPECTER_DEFAULT_LAYER = createSpecterRenderType("specter_default_layer", SpecterClient.specterShaderProgram);


    public static RenderLayer createSpecterRenderType(String name, Identifier shaderIdentifier) {
        return RenderLayer.of(
                name,
                VertexFormats.POSITION_TEXTURE_COLOR_LIGHT,
                VertexFormat.DrawMode.QUADS,
                256,
                false,
                true,
                RenderLayer.MultiPhaseParameters.builder()
                        .program(new RenderPhase.ShaderProgram(() -> SpecterShaderManager.getShaderProgram(shaderIdentifier))) // Custom shader setup
                        .texture(RenderPhase.BLOCK_ATLAS_TEXTURE)
                        .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                        .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                        .build(false)
        );
    }

    public SpecterRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }
}
