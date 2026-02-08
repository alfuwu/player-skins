package com.alfred.playerskins.mixin.client;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "updateCapeUrl", at = @At("HEAD"), cancellable = true)
    private void updateCapeUrl(CallbackInfo ci) {
        ci.cancel();
    }
}
