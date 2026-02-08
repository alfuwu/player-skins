package com.alfred.playerskins.mixin.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow protected abstract void notifyEntityRemoved(Entity entity);

    @Redirect(method = "tickEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;notifyEntityRemoved(Lnet/minecraft/entity/Entity;)V", ordinal = 1))
    private void thing(World instance, Entity entity) {
        if (entity instanceof PlayerEntity)
            return; // prevent de-loading player skins after they die (because we only want to de-load their skin when they leave the game)
        notifyEntityRemoved(entity);
    }
}
