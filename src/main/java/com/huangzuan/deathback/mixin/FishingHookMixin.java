package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.player.Player;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(FishingHook.class)
public class FishingHookMixin {
	@Inject(method = "retrieve", at = @At("HEAD"))
	private void onRetrieve(ItemStack stack, CallbackInfoReturnable<Integer> callback) {
		Player owner = ((FishingHook) (Object) this).getPlayerOwner();
		if (owner != null)
			DeathBackModTriggers.fireFishedItem(owner, stack);
	}
}