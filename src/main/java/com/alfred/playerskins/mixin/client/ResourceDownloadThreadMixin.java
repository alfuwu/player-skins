package com.alfred.playerskins.mixin.client;

import net.minecraft.client.resource.ResourceDownloadThread;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourceDownloadThread.class)
public abstract class ResourceDownloadThreadMixin {
    @Shadow public abstract void reload();

    // cancel obsolete thread
    // the s3 bucket for minecraft skins is long dead, all this does is throw an error
    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    private void stopThrowingErrorsPls(CallbackInfo ci) {
        this.reload();
        ci.cancel();
    }
}
