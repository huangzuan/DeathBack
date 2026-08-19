package com.huangzuan.deathback.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import com.huangzuan.deathback.network.DeathBackModVariables;

@EventBusSubscriber
public class RecordDeathLocationProcedure {
	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
		}
	}

	public static void execute(double x, double y, double z, Entity entity) {
		execute(null, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player) {
			{
				DeathBackModVariables.PlayerVariables _vars = entity.getData(DeathBackModVariables.PLAYER_VARIABLES);
				_vars.deathDimension = "" + entity.level().dimension();
				_vars.deathX = x;
				_vars.deathY = y;
				_vars.deathZ = z;
				_vars.markSyncDirty();
			}
		}
	}
}