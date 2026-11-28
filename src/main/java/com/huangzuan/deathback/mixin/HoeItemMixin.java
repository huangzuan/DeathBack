package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.InteractionResult;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(HoeItem.class)
public class HoeItemMixin {
	@Inject(method = "useOn", at = @At("HEAD"))
	private void onUseOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> callback) {
		if (context.getPlayer() != null)
			DeathBackModTriggers.fireUseHoe(context.getPlayer(), context.getClickedPos(), context.getLevel().getBlockState(context.getClickedPos()));
	}
}