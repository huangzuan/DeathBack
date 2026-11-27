package com.huangzuan.deathback.procedures;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.core.BlockPos;

import com.huangzuan.deathback.network.DeathBackModVariables;

public class TeleportToDeathLocationProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			Entity _ent = entity;
			double _tx = entity.getCapability(DeathBackModVariables.PLAYER_VARIABLES).orElseGet(DeathBackModVariables.PlayerVariables::new).deathX;
			double _ty = entity.getCapability(DeathBackModVariables.PLAYER_VARIABLES).orElseGet(DeathBackModVariables.PlayerVariables::new).deathY;
			double _tz = entity.getCapability(DeathBackModVariables.PLAYER_VARIABLES).orElseGet(DeathBackModVariables.PlayerVariables::new).deathZ;
			_ent.teleportTo(_tx, _ty, _tz);
			if (_ent instanceof ServerPlayer _serverPlayer)
				_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
		}
		if (entity.getCapability(DeathBackModVariables.PLAYER_VARIABLES).orElseGet(DeathBackModVariables.PlayerVariables::new).deathDimension.contains("nether")) {
			if (entity instanceof ServerPlayer _player && !_player.level().isClientSide()) {
				ResourceKey<Level> destinationType = Level.NETHER;
				if (_player.level().dimension() == destinationType)
					return;
				ServerLevel nextLevel = _player.server.getLevel(destinationType);
				if (nextLevel != null) {
					_player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
					_player.teleportTo(nextLevel, _player.getX(), _player.getY(), _player.getZ(), _player.getYRot(), _player.getXRot());
					_player.connection.send(new ClientboundPlayerAbilitiesPacket(_player.getAbilities()));
					for (MobEffectInstance _effectinstance : _player.getActiveEffects())
						_player.connection.send(new ClientboundUpdateMobEffectPacket(_player.getId(), _effectinstance));
					_player.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
				}
			}
		} else if (entity.getCapability(DeathBackModVariables.PLAYER_VARIABLES).orElseGet(DeathBackModVariables.PlayerVariables::new).deathDimension.contains("end")) {
			if (entity instanceof ServerPlayer _player && !_player.level().isClientSide()) {
				ResourceKey<Level> destinationType = Level.END;
				if (_player.level().dimension() == destinationType)
					return;
				ServerLevel nextLevel = _player.server.getLevel(destinationType);
				if (nextLevel != null) {
					_player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
					_player.teleportTo(nextLevel, _player.getX(), _player.getY(), _player.getZ(), _player.getYRot(), _player.getXRot());
					_player.connection.send(new ClientboundPlayerAbilitiesPacket(_player.getAbilities()));
					for (MobEffectInstance _effectinstance : _player.getActiveEffects())
						_player.connection.send(new ClientboundUpdateMobEffectPacket(_player.getId(), _effectinstance));
					_player.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
				}
			}
		} else if (entity.getCapability(DeathBackModVariables.PLAYER_VARIABLES).orElseGet(DeathBackModVariables.PlayerVariables::new).deathDimension.contains("surface")) {
			if (entity instanceof ServerPlayer _player && !_player.level().isClientSide()) {
				ResourceKey<Level> destinationType = Level.OVERWORLD;
				if (_player.level().dimension() == destinationType)
					return;
				ServerLevel nextLevel = _player.server.getLevel(destinationType);
				if (nextLevel != null) {
					_player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
					_player.teleportTo(nextLevel, _player.getX(), _player.getY(), _player.getZ(), _player.getYRot(), _player.getXRot());
					_player.connection.send(new ClientboundPlayerAbilitiesPacket(_player.getAbilities()));
					for (MobEffectInstance _effectinstance : _player.getActiveEffects())
						_player.connection.send(new ClientboundUpdateMobEffectPacket(_player.getId(), _effectinstance));
					_player.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
				}
			}
		} else {
			if (entity instanceof net.minecraft.server.level.ServerPlayer _player) {
				net.minecraft.server.MinecraftServer _mcServer = _player.getServer();
				if (_mcServer != null) {
					// 1. Forge 1.20.1 标准 Capability 获取变量方式
					_player.getCapability(DeathBackModVariables.PLAYER_VARIABLES, null).ifPresent(vars -> {
						String rawDimStr = vars.deathDimension;
						if (rawDimStr != null && !rawDimStr.isEmpty()) {
							// 2. 清洗字符串
							String cleanDimStr = rawDimStr;
							if (cleanDimStr.contains("/")) {
								cleanDimStr = cleanDimStr.substring(cleanDimStr.lastIndexOf("/") + 1).trim();
							}
							if (cleanDimStr.contains("]")) {
								cleanDimStr = cleanDimStr.replace("]", "").trim();
							}
							// 3. Forge 1.20.1 兼容的 ResourceLocation 与传送逻辑
							net.minecraft.resources.ResourceLocation _dimLoc = new net.minecraft.resources.ResourceLocation(cleanDimStr);
							net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> _destinationKey = net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, _dimLoc);
							net.minecraft.server.level.ServerLevel _targetLevel = _mcServer.getLevel(_destinationKey);
							if (_targetLevel != null) {
								double targetX = vars.deathX;
								double targetY = vars.deathY;
								double targetZ = vars.deathZ;
								_player.teleportTo(_targetLevel, targetX, targetY, targetZ, _player.getYRot(), _player.getXRot());
							}
						}
					});
				}
			}
		}
	}
}