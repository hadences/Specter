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
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class SpecterParticleSheets {

    private static final Map<Identifier, ParticleTextureSheet> particleTextureSheetMap = new HashMap<>();

    /**
     * adds a custom particle sheet
     * @param identifier the identifier of the shader
     * @param sheet the particle texture sheet
     */
    public static void addCustomSheet(Identifier identifier, ParticleTextureSheet sheet){
        particleTextureSheetMap.put(identifier, sheet);
    }

    /**
     * returns the map of custom particle texture sheets
     * @return the map of custom particle texture sheets
     */
    public static Map<Identifier, ParticleTextureSheet> getParticleTextureSheetMap(){
        return particleTextureSheetMap;
    }

    public static ParticleTextureSheet SPECTER_PARTICLE_SHEET = new ParticleTextureSheet() {
        public BufferBuilder begin(Tessellator tessellator, TextureManager textureManager) {
            RenderSystem.depthMask(true);

            RenderSystem.setShader(GameRenderer::getParticleProgram);
//            RenderSystem.setShader(() -> SpecterShaderManager.getShaderProgram(SpecterClient.specterShaderProgram));
            RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            return tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
        }

        public String toString() {
            return "SPECTER_PARTICLE_SHEET";
        }
    };

    public static void reloadCustomSheets(){
        List<ParticleTextureSheet> mutableSheets = new ArrayList<>(ParticleManager.PARTICLE_TEXTURE_SHEETS);

        for(ParticleTextureSheet sheet : particleTextureSheetMap.values()){
            mutableSheets.removeIf(s -> s == sheet);
            if(!mutableSheets.contains(sheet)){
                mutableSheets.add(sheet);
                Specter.LOGGER.info("Custom particle sheet registered.");
            } else {
                Specter.LOGGER.info("Custom particle sheet already registered.");
            }
        }

        ParticleManager.PARTICLE_TEXTURE_SHEETS = mutableSheets;
    }
}
