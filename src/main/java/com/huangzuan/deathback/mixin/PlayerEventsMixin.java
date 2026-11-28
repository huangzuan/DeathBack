package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(Player.class)
public class PlayerEventsMixin {
	@Inject(method = "stopSleepInBed", at = @At("HEAD"))
	private void onWakeUp(boolean skipSleepTimer, boolean updateLevelForSleepingPlayers, CallbackInfo callback) {
		DeathBackModTriggers.fireWakesUp((Player) (Object) this);
	}

	@Inject(method = "giveExperiencePoints", at = @At("HEAD"))
	private void onXpChange(int amount, CallbackInfo callback) {
		DeathBackModTriggers.fireXpChange((Player) (Object) this, amount);
	}

	@Inject(method = "giveExperienceLevels", at = @At("HEAD"))
	private void onXpLevelChange(int levels, CallbackInfo callback) {
		DeathBackModTriggers.fireXpLevelChange((Player) (Object) this, levels);
	}

	@Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("RETURN"))
	private void onDrop(ItemStack stack, boolean dropAround, boolean traceItem, CallbackInfoReturnable<ItemEntity> callback) {
		ItemEntity dropped = callback.getReturnValue();
		if (dropped != null)
			DeathBackModTriggers.fireGemDropped((Player) (Object) this, dropped.getItem());
	}
}