package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.advancements.AdvancementHolder;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {
	@Shadow
	private ServerPlayer player;

	@Inject(method = "award", at = @At("RETURN"))
	private void onAward(AdvancementHolder advancement, String criterion, CallbackInfoReturnable<Boolean> callback) {
		if (callback.getReturnValueZ() && this.player != null)
			DeathBackModTriggers.fireAdvancement(this.player, advancement);
	}
}