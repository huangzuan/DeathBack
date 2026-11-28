package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(BlockItem.class)
public class BlockItemMixin {
	@Inject(method = "place", at = @At("RETURN"))
	private void onPlace(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> callback) {
		if (callback.getReturnValue() != null && callback.getReturnValue().consumesAction()) {
			BlockPos pos = context.getClickedPos();
			DeathBackModTriggers.fireBlockPlace(context.getLevel(), pos, context.getLevel().getBlockState(pos), context.getLevel().getBlockState(pos.relative(context.getClickedFace().getOpposite())), context.getPlayer());
		}
	}
}