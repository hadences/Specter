package net.hadences.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.hadences.Specter;
import net.hadences.SpecterClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class SpecterParticleSheets {
    public static ParticleTextureSheet SPECTER_PARTICLE_SHEET = new ParticleTextureSheet() {
        public BufferBuilder begin(Tessellator tessellator, TextureManager textureManager) {
            RenderSystem.depthMask(true);
//            RenderSystem.setShader(GameRenderer::getParticleProgram);
//            RenderSystem.setShader(() -> {
//                ShaderProgram shaderProgram = SpecterClient.getTestShaderProgram();
//                if (shaderProgram == null) {
//                    Specter.LOGGER.warn("Custom shader program is null, using default particle shader.");
//                    return GameRenderer.getParticleProgram();
//                }
//                return shaderProgram;
//            });
            RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            return tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
        }

        public String toString() {
            return "SPECTER_PARTICLE_SHEET";
        }
    };

    public static void init(){
        List<ParticleTextureSheet> mutableSheets = new ArrayList<>(ParticleManager.PARTICLE_TEXTURE_SHEETS);
        mutableSheets.removeIf(sheet -> sheet == SPECTER_PARTICLE_SHEET);
        if (!mutableSheets.contains(SPECTER_PARTICLE_SHEET)) {
            mutableSheets.add(SPECTER_PARTICLE_SHEET);
            ParticleManager.PARTICLE_TEXTURE_SHEETS = mutableSheets;
            Specter.LOGGER.info("Custom particle sheet registered.");
        } else {
            Specter.LOGGER.info("Custom particle sheet already registered.");
        }
    }
}
