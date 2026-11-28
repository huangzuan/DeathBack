package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Minecraft;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(Minecraft.class)
public class MinecraftUseItemMixin {
	@Shadow
	public LocalPlayer player;

	@Inject(method = "startUseItem", at = @At("HEAD"))
	private void onStartUseItem(CallbackInfo callback) {
		if (this.player != null && this.player.getMainHandItem().isEmpty())
			DeathBackModTriggers.fireRightClickEmptyHand(this.player);
	}
}