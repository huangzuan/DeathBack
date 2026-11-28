package com.huangzuan.deathback.procedures;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;

import com.huangzuan.deathback.network.DeathBackModVariables;

public class TeleportToDeathLocationProcedure {
	public static void execute(Entity entity) {
		if (!(entity instanceof ServerPlayer player))
			return;
		net.minecraft.server.MinecraftServer server = player.getServer();
		if (server == null)
			return;

		net.minecraft.nbt.CompoundTag data = player.getAttachedOrCreate(DeathBackModVariables.PLAYER_VARIABLES, net.minecraft.nbt.CompoundTag::new);
		double targetX = data.getDouble("deathX");
		double targetY = data.getDouble("deathY");
		double targetZ = data.getDouble("deathZ");
		String rawDimension = data.getString("deathDimension");
		ResourceKey<Level> targetDimension = null;

		boolean missingSavedLocation = rawDimension == null || rawDimension.isBlank() || rawDimension.equals("null") || (targetX == 0 && targetY == 0 && targetZ == 0);
		if (missingSavedLocation) {
			java.util.Optional<net.minecraft.core.GlobalPos> lastDeath = player.getLastDeathLocation();
			if (lastDeath.isEmpty()) {
				com.huangzuan.deathback.DeathBackMod.LOGGER.warn("/back has no saved death coordinates or vanilla last-death location for {}", player.getGameProfile().getName());
				return;
			}
			net.minecraft.core.GlobalPos deathLocation = lastDeath.get();
			targetDimension = deathLocation.dimension();
			targetX = deathLocation.pos().getX() + 0.5;
			targetY = deathLocation.pos().getY();
			targetZ = deathLocation.pos().getZ() + 0.5;
			com.huangzuan.deathback.DeathBackMod.LOGGER.info("/back using vanilla last-death location: x={}, y={}, z={}, dimension={}", targetX, targetY, targetZ, targetDimension);
		} else {
			String cleanDimension = rawDimension;
			if (cleanDimension.contains("/"))
				cleanDimension = cleanDimension.substring(cleanDimension.lastIndexOf("/") + 1).trim();
			cleanDimension = cleanDimension.replace("]", "").trim();
			for (ServerLevel level : server.getAllLevels()) {
				if (level.dimension().toString().contains(cleanDimension)) {
					targetDimension = level.dimension();
					break;
				}
			}
		}

		ServerLevel targetLevel = targetDimension == null ? null : server.getLevel(targetDimension);
		if (targetLevel == null) {
			com.huangzuan.deathback.DeathBackMod.LOGGER.warn("/back could not resolve death dimension {} for {}", rawDimension, player.getGameProfile().getName());
			return;
		}
		com.huangzuan.deathback.DeathBackMod.LOGGER.info("/back teleporting to x={}, y={}, z={}, dimension={}", targetX, targetY, targetZ, targetDimension);
		player.teleportTo(targetLevel, targetX, targetY, targetZ, player.getYRot(), player.getXRot());
	}
}
