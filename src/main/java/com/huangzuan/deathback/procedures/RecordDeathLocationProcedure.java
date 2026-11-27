package com.huangzuan.deathback.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

import com.huangzuan.deathback.network.DeathBackModVariables;

public class RecordDeathLocationProcedure {
	public static boolean eventResult = true;

	public RecordDeathLocationProcedure() {
		ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, amount) -> {
			if (entity != null) {
				execute(entity.getX(), entity.getY(), entity.getZ(), entity);
			}
			boolean result = eventResult;
			eventResult = true;
			return result;
		});
	}

	public static void execute(double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player) {
			{
				DeathBackModVariables.PlayerVariables _vars = entity.getAttachedOrCreate(DeathBackModVariables.PLAYER_VARIABLES);
				_vars.deathDimension = "" + entity.level().dimension();
				_vars.deathX = x;
				_vars.deathY = y;
				_vars.deathZ = z;
				_vars.markSyncDirty();
			}
		}
	}
}