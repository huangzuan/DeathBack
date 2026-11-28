package com.huangzuan.deathback.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import com.huangzuan.deathback.network.DeathBackModVariables;

public class RecordDeathLocationProcedure {
	static {
		net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
			execute(entity.getX(), entity.getY(), entity.getZ(), entity);
		});
	}

	public RecordDeathLocationProcedure() {
	}

	public static void execute(double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player) {
			DeathBackModVariables.playerVars(entity).deathDimension = "" + entity.level().dimension();
			DeathBackModVariables.playerVars(entity).deathX = x;
			DeathBackModVariables.playerVars(entity).deathY = y;
			DeathBackModVariables.playerVars(entity).deathZ = z;
			DeathBackModVariables.markPlayerDirty(entity);
		}
	}
}
