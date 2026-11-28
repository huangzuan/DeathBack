package com.huangzuan.deathback;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraft.server.TickTask;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.api.ModInitializer;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;

import com.huangzuan.deathback.network.DeathBackModVariables;
import com.huangzuan.deathback.init.DeathBackModCommands;

public class DeathBackMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger(DeathBackMod.class);
	public static final String MODID = "death_back";

	@Override
	public void onInitialize() {
		// Start of user code block mod constructor
		// End of user code block mod constructor
		LOGGER.info("Initializing DeathBackMod");
		DeathBackModVariables.load();
		DeathBackModCommands.load();
		tick();
		new com.huangzuan.deathback.procedures.RecordDeathLocationProcedure();
		new com.huangzuan.deathback.procedures.TeleportToDeathLocationProcedure();
		new com.huangzuan.deathback.procedures.ModLoggerProcedure();
		// Start of user code block mod init
		// End of user code block mod init
	}

	// Start of user code block mod methods
	// End of user code block mod methods
	private record ScheduledTask(Runnable action, int delay) {
	}

	private record TickTask(int tick, Runnable action) {
	}

	private static final Queue<ScheduledTask> pendingTasks = new ConcurrentLinkedQueue<>();
	private static final PriorityQueue<TickTask> runningTasks = new PriorityQueue<>(Comparator.comparingInt(TickTask::tick));

	public static void queueServerWork(int delay, Runnable action) {
		pendingTasks.add(new ScheduledTask(action, delay));
	}

	private void tick() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			int currentTick = server.getTickCount();
			ScheduledTask scheduled;
			while ((scheduled = pendingTasks.poll()) != null) {
				runningTasks.add(new TickTask(currentTick + scheduled.delay(), scheduled.action()));
			}
			while (!runningTasks.isEmpty() && currentTick >= runningTasks.peek().tick()) {
				runningTasks.poll().action().run();
			}
		});
	}
	// Start of user code block mod static methods
	// End of user code block mod static methods
}