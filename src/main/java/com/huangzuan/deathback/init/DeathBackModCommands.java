/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.huangzuan.deathback.init;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.huangzuan.deathback.command.BackCommand;

public class DeathBackModCommands {
	public static void load() {
		CommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext, environment) -> {
			BackCommand.register(dispatcher, commandBuildContext, environment);
		});
	}
}