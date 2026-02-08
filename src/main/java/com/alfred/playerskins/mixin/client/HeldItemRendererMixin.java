package com.alfred.playerskins.mixin.client;

import com.alfred.playerskins.interfaces.AdvancedPlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicOverlayRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ArsenicOverlayRenderer.class, remap = false)
public abstract class HeldItemRendererMixin {
    // hell
    // theres certainly a more elegant way to do this but idc it works as is
    @Inject(
      method = "renderVanilla(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/modificationstation/stationapi/api/client/texture/SpriteAtlasTexture;)V",
      at = @At(
        value = "INVOKE",
        target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V"
      ),
      slice = @Slice(
        from = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glScalef(FFF)V"),
        to = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;startQuads()V")
      )
    )
    private void moveItem(LivingEntity entity, ItemStack item, SpriteAtlasTexture atlas, CallbackInfo ci) {
        if (entity instanceof AdvancedPlayerModel adv && adv.isSlim())
            GL11.glTranslatef(0, 0, 0.05f);
    }
}
