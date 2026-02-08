package com.alfred.playerskins.mixin.client;

import com.alfred.playerskins.Util;
import com.alfred.playerskins.interfaces.AdvancedPlayerModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.Session;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements AdvancedPlayerModel {
    @Unique public boolean slim;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fixSkinTexture(Minecraft world, World session, Session dimensionId, int par4, CallbackInfo ci) {
        ((PlayerEntity)(Object)this).name = "slyprae";
        Util.loadTextures((PlayerEntity)(Object)this);
    }

    @Unique
    @Override
    public boolean isSlim() {
        return slim;
    }

    @Unique
    @Override
    public void setSlim(boolean bl) {
        slim = bl;
    }
}
