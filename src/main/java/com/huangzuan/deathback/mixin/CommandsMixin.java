package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.ParseResults;

import com.huangzuan.deathback.event.MiscEvents;

@Mixin(Commands.class)
public abstract class CommandsMixin {
	@Inject(method = "performCommand(Lcom/mojang/brigadier/ParseResults;Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
	public void performCommand(ParseResults<CommandSourceStack> parseResults, String string, CallbackInfo ci) {
		if (!MiscEvents.COMMAND_EXECUTE.invoker().onCommandExecuted(parseResults))
			ci.cancel();
	}
}