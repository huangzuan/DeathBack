package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

import java.util.function.Consumer;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V", at = @At("RETURN"))
	private void onDamage(int amount, ServerLevel level, ServerPlayer player, Consumer<Item> onBroken, CallbackInfo callback) {
		ItemStack stack = (ItemStack) (Object) this;
		if (stack.getCount() <= 0)
			DeathBackModTriggers.fireItemDestroyed(player, stack);
	}
}