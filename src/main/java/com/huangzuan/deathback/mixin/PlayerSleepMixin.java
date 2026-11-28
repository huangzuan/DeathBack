package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Unit;
import net.minecraft.core.BlockPos;

import com.mojang.datafixers.util.Either;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(Player.class)
public class PlayerSleepMixin {
	@Inject(method = "startSleepInBed", at = @At("RETURN"))
	private void onSleep(BlockPos pos, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> callback) {
		if (callback.getReturnValue() != null && callback.getReturnValue().left().isEmpty())
			DeathBackModTriggers.fireInBed((Player) (Object) this, pos);
	}
}