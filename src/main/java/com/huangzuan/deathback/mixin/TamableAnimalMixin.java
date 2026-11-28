package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(TamableAnimal.class)
public class TamableAnimalMixin {
	@Inject(method = "tame", at = @At("RETURN"))
	private void onTame(Player player, CallbackInfo callback) {
		DeathBackModTriggers.fireTamed((TamableAnimal) (Object) this, player);
	}
}