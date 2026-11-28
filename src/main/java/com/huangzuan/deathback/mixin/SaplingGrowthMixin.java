package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(TreeGrower.class)
public class SaplingGrowthMixin {
	@Inject(method = "growTree", at = @At("HEAD"), cancellable = true)
	private void onGrowTree(ServerLevel level, ChunkGenerator generator, BlockPos pos, BlockState state, RandomSource random, CallbackInfoReturnable<Boolean> callback) {
		if (!DeathBackModTriggers.fireSaplingGrowth(level, pos, state))
			callback.setReturnValue(false);
	}
}