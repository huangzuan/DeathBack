package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.ParseResults;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(Commands.class)
public class CommandManagerMixin {
	@Inject(method = "performCommand", at = @At("HEAD"))
	private void onCommand(ParseResults<CommandSourceStack> parse, String command, CallbackInfo callback) {
		DeathBackModTriggers.fireCommand(parse.getContext().getSource(), command, parse.getContext().build(command));
	}
}