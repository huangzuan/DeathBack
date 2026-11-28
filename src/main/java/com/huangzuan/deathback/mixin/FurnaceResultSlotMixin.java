package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.entity.player.Player;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(FurnaceResultSlot.class)
public class FurnaceResultSlotMixin {
	@Inject(method = "onTake", at = @At("RETURN"))
	private void onTake(Player player, ItemStack stack, CallbackInfo callback) {
		DeathBackModTriggers.fireItemSmelted(player, stack);
	}
}