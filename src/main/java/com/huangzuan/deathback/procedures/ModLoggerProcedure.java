package com.huangzuan.deathback.procedures;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import javax.annotation.Nullable;

import com.huangzuan.deathback.DeathBackMod;

@EventBusSubscriber
public class ModLoggerProcedure {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		execute();
	}

	public static void execute() {
		execute(null);
	}

	private static void execute(@Nullable Event event) {
		DeathBackMod.LOGGER.info("+-------------------------------------------------------+");
		DeathBackMod.LOGGER.info("|  ___  ____    _  _____ _   _   ____   _    ____ _  __ |");
		DeathBackMod.LOGGER.info("| |  _ \\|  __|  / \\|_   _| |_| | |  _ \\ / \\  / ___| |/ /|");
		DeathBackMod.LOGGER.info("| | | | |  __| / _ \\ | | |  _  | |  _  / _ \\ | |__ | ' / |");
		DeathBackMod.LOGGER.info("| | |_| | |___/ ___ \\| | | | | | | |_) / ___ \\| |__| . \\ |");
		DeathBackMod.LOGGER.info("| |____/|____/_/   \\_\\_| |_| |_| |____/_/   \\_\\___|_|\\_\\|");
		DeathBackMod.LOGGER.info("|                         Death Back  v0.2-Alpha        |");
		DeathBackMod.LOGGER.info("+-------------------------------------------------------+");
	}
}