package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(Player.class)
public class PlayerCriticalMixin {
	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private void onAttack(Entity target, CallbackInfo callback) {
		Player player = (Player) (Object) this;
		if (!DeathBackModTriggers.fireCriticalHit(player, target))
			callback.cancel();
	}
}