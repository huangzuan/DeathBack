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

import java.util.Set;

import com.huangzuan.deathback.network.DeathBackModVariables;

public class TeleportToDeathLocationProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			Entity _ent = entity;
			double _tx = entity.getAttachedOrCreate(DeathBackModVariables.PLAYER_VARIABLES).deathX;
			double _ty = entity.getAttachedOrCreate(DeathBackModVariables.PLAYER_VARIABLES).deathY;
			double _tz = entity.getAttachedOrCreate(DeathBackModVariables.PLAYER_VARIABLES).deathZ;
			_ent.teleportTo(_tx, _ty, _tz);
			if (_ent instanceof ServerPlayer _serverPlayer)
				_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
		}
		if (entity.getAttachedOrCreate(DeathBackModVariables.PLAYER_VARIABLES).deathDimension.contains("nether")) {
			if (entity instanceof ServerPlayer _player && _player.level() instanceof ServerLevel _serverLevel) {
				ResourceKey<Level> destinationType = Level.NETHER;
				if (_player.level().dimension() == destinationType)
					return;
				ServerLevel nextLevel = _serverLevel.getServer().getLevel(destinationType);
				if (nextLevel != null) {
					_player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
					_player.teleportTo(nextLevel, _player.getX(), _player.getY(), _player.getZ(), Set.of(), _player.getYRot(), _player.getXRot(), true);
					_player.connection.send(new ClientboundPlayerAbilitiesPacket(_player.getAbilities()));
					for (MobEffectInstance _effectinstance : _player.getActiveEffects())
						_player.connection.send(new ClientboundUpdateMobEffectPacket(_player.getId(), _effectinstance, false));
					_player.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
				}
			}
		} else if (entity.getAttachedOrCreate(DeathBackModVariables.PLAYER_VARIABLES).deathDimension.contains("end")) {
			if (entity instanceof ServerPlayer _player && _player.level() instanceof ServerLevel _serverLevel) {
				ResourceKey<Level> destinationType = Level.END;
				if (_player.level().dimension() == destinationType)
					return;
				ServerLevel nextLevel = _serverLevel.getServer().getLevel(destinationType);
				if (nextLevel != null) {
					_player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
					_player.teleportTo(nextLevel, _player.getX(), _player.getY(), _player.getZ(), Set.of(), _player.getYRot(), _player.getXRot(), true);
					_player.connection.send(new ClientboundPlayerAbilitiesPacket(_player.getAbilities()));
					for (MobEffectInstance _effectinstance : _player.getActiveEffects())
						_player.connection.send(new ClientboundUpdateMobEffectPacket(_player.getId(), _effectinstance, false));
					_player.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
				}
			}
		} else if (entity.getAttachedOrCreate(DeathBackModVariables.PLAYER_VARIABLES).deathDimension.contains("surface")) {
			if (entity instanceof ServerPlayer _player && _player.level() instanceof ServerLevel _serverLevel) {
				ResourceKey<Level> destinationType = Level.OVERWORLD;
				if (_player.level().dimension() == destinationType)
					return;
				ServerLevel nextLevel = _serverLevel.getServer().getLevel(destinationType);
				if (nextLevel != null) {
					_player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
					_player.teleportTo(nextLevel, _player.getX(), _player.getY(), _player.getZ(), Set.of(), _player.getYRot(), _player.getXRot(), true);
					_player.connection.send(new ClientboundPlayerAbilitiesPacket(_player.getAbilities()));
					for (MobEffectInstance _effectinstance : _player.getActiveEffects())
						_player.connection.send(new ClientboundUpdateMobEffectPacket(_player.getId(), _effectinstance, false));
					_player.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
				}
			}
		} else {
			if (entity instanceof net.minecraft.server.level.ServerPlayer _player) {
				net.minecraft.server.MinecraftServer _mcServer = _player.level().getServer();
				if (_mcServer != null) {
					try {
						// 读取 MCreator 玩家持久化变量
						com.huangzuan.deathback.network.DeathBackModVariables.PlayerVariables _vars = _player.getAttachedOrCreate(com.huangzuan.deathback.network.DeathBackModVariables.PLAYER_VARIABLES);
						// 1. 获取死亡维度字符串
						String rawDimStr = _vars.deathDimension;
						if (rawDimStr != null && !rawDimStr.isEmpty() && !rawDimStr.equals("null")) {
							String cleanDimStr = rawDimStr;
							if (cleanDimStr.contains("/")) {
								cleanDimStr = cleanDimStr.substring(cleanDimStr.lastIndexOf("/") + 1).trim();
							}
							if (cleanDimStr.contains("]")) {
								cleanDimStr = cleanDimStr.replace("]", "").trim();
							}
							// 2. 遍历服务器中所有可用维度进行匹配
							net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> _destinationKey = null;
							for (net.minecraft.server.level.ServerLevel level : _mcServer.getAllLevels()) {
								String dimKeyStr = level.dimension().toString();
								if (dimKeyStr.contains(cleanDimStr) || cleanDimStr.contains(level.dimension().registry().toString())) {
									_destinationKey = level.dimension();
									break;
								}
							}
							if (_destinationKey != null) {
								net.minecraft.server.level.ServerLevel _targetLevel = _mcServer.getLevel(_destinationKey);
								if (_targetLevel != null) {
									// 3. 获取玩家保存的死亡坐标
									double targetX = _vars.deathX;
									double targetY = _vars.deathY;
									double targetZ = _vars.deathZ;
									// 4. 执行 Fabric 传送
									_player.teleportTo(_targetLevel, targetX, targetY, targetZ, java.util.Set.of(), _player.getYRot(), _player.getXRot(), false);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}