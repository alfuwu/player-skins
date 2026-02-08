package com.alfred.playerskins.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.texture.SkinImageProcessor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.image.BufferedImage;

@Mixin(SkinImageProcessor.class)
public class SkinImageProcessorMixin {
    @Shadow private int height;

    // you don't wanna know how long it took me to realize that minecraft
    // was cropping the player texture to be only 32 pixels in height and
    // then wrapping the uvs around when they went beyond 32px
    @Redirect(method = "process", at = @At(value = "FIELD", target = "Lnet/minecraft/client/texture/SkinImageProcessor;height:I", opcode = Opcodes.PUTFIELD))
    private void modifyImageHeight(SkinImageProcessor instance, int value, @Local(argsOnly = true) BufferedImage img) {
        height = img.getHeight() > 32 ? 64 : 32;
    }
}
