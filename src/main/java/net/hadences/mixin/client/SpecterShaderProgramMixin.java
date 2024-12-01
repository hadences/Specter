package net.hadences.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShaderProgram.class)
public class SpecterShaderProgramMixin {
    @Redirect(
            method = "<init>(Lnet/minecraft/resource/ResourceFactory;Ljava/lang/String;Lnet/minecraft/client/render/VertexFormat;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"
            )
    )
    private Identifier redirectIdentifier(String path) {
        // 'path' is "shaders/core/" + name + ".json"
        // We need to extract the namespace from 'name' if present
        String adjustedPath = path;
        String namespace = "minecraft"; // Default namespace

        if (path.startsWith("shaders/core/")) {
            String nameWithExtension = path.substring("shaders/core/".length());
            if (nameWithExtension.endsWith(".json")) {
                String name = nameWithExtension.substring(0, nameWithExtension.length() - ".json".length());
                String[] parts = name.split(":", 2);
                if (parts.length == 2) {
                    namespace = parts[0];
                    name = parts[1];
                    adjustedPath = "shaders/core/" + name + ".json";
                }
            }
        }

        Identifier customId = Identifier.of(namespace, adjustedPath);

        // Use the resource manager to check if the resource exists
        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();

        try {
            resourceManager.getResource(customId);
        } catch (Exception e) {
            // Resource does not exist
            customId = Identifier.ofVanilla(path);
        }

        return customId;
    }

}
