package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(Entity.class)
public class EntityThunderMixin {
	@Inject(method = "thunderHit", at = @At("HEAD"))
	private void onThunderHit(ServerLevel level, LightningBolt bolt, CallbackInfo callback) {
		DeathBackModTriggers.fireStruckByLightning((Entity) (Object) this, bolt);
	}
}