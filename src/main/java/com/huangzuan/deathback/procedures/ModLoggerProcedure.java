package com.huangzuan.deathback.procedures;

import com.huangzuan.deathback.DeathBackMod;

public class ModLoggerProcedure {
	public static void execute() {
		DeathBackMod.LOGGER.info("+-------------------------------------------------------+");
		DeathBackMod.LOGGER.info("|  ___  ____    _  _____ _   _   ____   _    ____ _  __ |");
		DeathBackMod.LOGGER.info("| |  _ \\|  __|  / \\|_   _| |_| | |  _ \\ / \\  / ___| |/ /|");
		DeathBackMod.LOGGER.info("| | | | |  __| / _ \\ | | |  _  | |  _  / _ \\ | |__ | ' / |");
		DeathBackMod.LOGGER.info("| | |_| | |___/ ___ \\| | | | | | | |_) / ___ \\| |__| . \\ |");
		DeathBackMod.LOGGER.info("| |____/|____/_/   \\_\\_| |_| |_| |____/_/   \\_\\___|_|\\_\\|");
		DeathBackMod.LOGGER.info("|                         Death Back  v0.1-Beta        |");
		DeathBackMod.LOGGER.info("+-------------------------------------------------------+");
	}
}