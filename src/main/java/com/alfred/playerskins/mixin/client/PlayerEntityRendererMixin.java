package com.alfred.playerskins.mixin.client;

import com.alfred.playerskins.config.Config;
import com.alfred.playerskins.events.init.InitListener;
import com.alfred.playerskins.interfaces.AdvancedPlayerModel;
import com.alfred.playerskins.render.PlayerEntityModel;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.texture.ImageDownload;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer {
    @Shadow private BipedEntityModel bipedModel;
    @Unique @Final private static final PlayerEntityModel DEFAULT_MODEL = new PlayerEntityModel(false, false);
    @Unique @Final private static final PlayerEntityModel SLIM_MODEL = new PlayerEntityModel(true, false);
    @Unique @Final private static final PlayerEntityModel DEFAULT_CLASSIC = new PlayerEntityModel(false, true);
    @Unique @Final private static final PlayerEntityModel SLIM_CLASSIC = new PlayerEntityModel(true, true); // don't actually know if this can even exist but whatever

    public PlayerEntityRendererMixin(EntityModel entityModel, float shadowRadius) {
        super(entityModel, shadowRadius);
    }

    @Inject(method = "render(Lnet/minecraft/entity/player/PlayerEntity;DDDFF)V", at = @At("HEAD"))
    private void changeModelRender(PlayerEntity d, double e, double f, double g, float h, float par6, CallbackInfo ci) {
        changeModel(d);
    }

    @Inject(method = "renderHand", at = @At("HEAD"))
    private void changeModelHand(CallbackInfo ci) {
        changeModel(MinecraftAccessor.getInstance().player);
    }

    @Unique
    private boolean isClassic(String url) {
        if (this.dispatcher.textureManager == null)
            return true;
        ImageDownload var3 = (ImageDownload)((TextureManagerAccessor)this.dispatcher.textureManager).getDownloadedImages().get(url);
        return var3 == null || var3.image == null || var3.image.getHeight() <= 32;
    }

    @Unique
    private void changeModel(PlayerEntity player) {
        // evil model changing code
        boolean slim = ((AdvancedPlayerModel)player).isSlim() && Config.allowSlim;
        this.model = this.bipedModel = isClassic(player.skinUrl) ? slim ? SLIM_CLASSIC : DEFAULT_CLASSIC : slim ? SLIM_MODEL : DEFAULT_MODEL;
    }
}
