package com.huangzuan.deathback.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.client.Minecraft;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

import com.huangzuan.deathback.DeathBackMod;

public class DeathBackModVariables {
	public static final AttachmentType<CompoundTag> PLAYER_VARIABLES = AttachmentRegistry.createPersistent(ResourceLocation.fromNamespaceAndPath(DeathBackMod.MODID, "player_variables"), CompoundTag.CODEC);
	private static final java.util.Map<java.util.UUID, PlayerVariables> PLAYER_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

	public static PlayerVariables playerVars(net.minecraft.world.entity.Entity entity) {
		if (!(entity instanceof net.minecraft.world.entity.player.Player player))
			return new PlayerVariables();
		java.util.UUID id = player.getUUID();
		if (player.level() instanceof ServerLevel) {
			CompoundTag tag = player.getAttachedOrCreate(PLAYER_VARIABLES, CompoundTag::new);
			PlayerVariables vars = new PlayerVariables();
			vars.deserializeNBT(player.registryAccess(), tag);
			PLAYER_CACHE.put(id, vars);
			return vars;
		}
		return PLAYER_CACHE.computeIfAbsent(id, key -> new PlayerVariables());
	}

	public static void markPlayerDirty(net.minecraft.world.entity.Entity entity) {
		if (entity instanceof net.minecraft.world.entity.player.Player player) {
			PlayerVariables vars = PLAYER_CACHE.get(player.getUUID());
			if (vars != null) {
				CompoundTag tag = vars.serializeNBT(player.registryAccess());
				player.setAttached(PLAYER_VARIABLES, tag);
				if (player instanceof ServerPlayer serverPlayer)
					ServerPlayNetworking.send(serverPlayer, new SavedDataSyncMessage(2, tag));
			}
		}
	}

	public static void load() {
		PayloadTypeRegistry.playS2C().register(SavedDataSyncMessage.TYPE, SavedDataSyncMessage.STREAM_CODEC);
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayer player = handler.getPlayer();
			PlayerVariables vars = new PlayerVariables();
			vars.deserializeNBT(player.registryAccess(), player.getAttachedOrCreate(PLAYER_VARIABLES, CompoundTag::new));
			PLAYER_CACHE.put(player.getUUID(), vars);
			ServerPlayNetworking.send(player, new SavedDataSyncMessage(2, vars.serializeNBT(player.registryAccess())));
		});
	}

	@Environment(EnvType.CLIENT)
	public static void loadClient() {
		ClientPlayNetworking.registerGlobalReceiver(SavedDataSyncMessage.TYPE, (payload, context) -> SavedDataSyncMessage.handleData(payload, context.client()));
	}

	public static class PlayerVariables {
		public double deathX = 0;
		public double deathY = 0;
		public double deathZ = 0;
		public String deathDimension = "\"minecraft:overworld\"";

		public CompoundTag serializeNBT(HolderLookup.Provider lookupProvider) {
			CompoundTag nbt = new CompoundTag();
			nbt.putDouble("deathX", deathX);
			nbt.putDouble("deathY", deathY);
			nbt.putDouble("deathZ", deathZ);
			nbt.putString("deathDimension", deathDimension);
			return nbt;
		}

		public void deserializeNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
			deathX = nbt.getDouble("deathX");
			deathY = nbt.getDouble("deathY");
			deathZ = nbt.getDouble("deathZ");
			deathDimension = nbt.getString("deathDimension");
		}
	}

	public record SavedDataSyncMessage(int dataType, CompoundTag data) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<SavedDataSyncMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DeathBackMod.MODID, "saved_data_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, SavedDataSyncMessage> STREAM_CODEC = new StreamCodec<>() {
			@Override
			public SavedDataSyncMessage decode(RegistryFriendlyByteBuf buffer) {
				return new SavedDataSyncMessage(buffer.readInt(), buffer.readNbt());
			}

			@Override
			public void encode(RegistryFriendlyByteBuf buffer, SavedDataSyncMessage message) {
				buffer.writeInt(message.dataType());
				buffer.writeNbt(message.data());
			}
		};

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}

		@Environment(EnvType.CLIENT)
		public static void handleData(SavedDataSyncMessage message, Minecraft client) {
			client.execute(() -> {
				if (client.level == null || message.data() == null)
					return;
				if (message.dataType() == 2 && client.player != null) {
					PlayerVariables vars = PLAYER_CACHE.computeIfAbsent(client.player.getUUID(), key -> new PlayerVariables());
					vars.deserializeNBT(client.level.registryAccess(), message.data());
				}
			});
		}
	}
}