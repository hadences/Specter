package net.hadences.mixin.client;

import net.hadences.Specter;
import net.hadences.render.SpecterShaderManager;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShaderProgram.class)
public class SpecterShaderProgramMixin {

//    @Redirect(method = "<init>", at = @At(value = "INVOKE",
//            target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"))
//    private Identifier redirectConstructorIdentifierOfVanilla(String path) {
//        if (isCustomShaderPath(path)) {
//            Specter.LOGGER.info("Redirecting shader path: {}", path);
//            return Identifier.of(SpecterShaderManager.getModNamespace(), path);
//        } else {
//            return Identifier.ofVanilla(path);
//        }
//    }
//
//    @Redirect(method = "loadShader", at = @At(value = "INVOKE",
//            target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"))
//    private static Identifier redirectLoadShaderIdentifierOfVanilla(String path) {
//        if (isCustomShaderPath(path)) {
//            return Identifier.of(SpecterShaderManager.getModNamespace(), path);
//        } else {
//            return Identifier.ofVanilla(path);
//        }
//    }
//
//    @Unique
//    private static boolean isCustomShaderPath(String path) {
//        String shaderName = path.substring("shaders/core/".length());
//        int start = shaderName.lastIndexOf("/") + 1; // Finds the last "/" and moves one position forward
//        int end = shaderName.lastIndexOf("."); // Finds the last "." for the file extension
//        String result = shaderName.substring(start, end);
//        return SpecterShaderManager.getCustomShaderNames().contains(result);
//    }

}
