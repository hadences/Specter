package net.hadences.render;

import net.hadences.SpecterClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

public class SpecterRenderLayer extends RenderLayer {
    private static final RenderLayer SPECTER_RENDER_LAYER = RenderLayer.of(
            "specter_render_layer",
            VertexFormats.POSITION_COLOR_TEXTURE_LIGHT,
            VertexFormat.DrawMode.QUADS,
            256,
            false,
            false,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(new RenderPhase.ShaderProgram(() -> SpecterClient.getDefaultShaderProgram()))
                    .texture(RenderPhase.BLOCK_ATLAS_TEXTURE)
                    .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                    .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                    .build(false)
    );

    public static RenderLayer getSpecterRenderLayer() {
        return SPECTER_RENDER_LAYER;
    }

    public SpecterRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }
}
