package com.huangzuan.deathback;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ClientModInitializer;

import com.huangzuan.deathback.network.DeathBackModVariables;

@Environment(EnvType.CLIENT)
public class DeathBackModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Start of user code block mod constructor
		// End of user code block mod constructor
		ClientPlayNetworking.registerGlobalReceiver(DeathBackModVariables.PlayerVariablesSyncMessage.TYPE, DeathBackModVariables.PlayerVariablesSyncMessage::handleData);
		// Start of user code block mod init
		// End of user code block mod init
	}
	// Start of user code block mod methods
	// End of user code block mod methods
}