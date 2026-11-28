package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;

import com.huangzuan.deathback.trigger.DeathBackModTriggers;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(method = "heal", at = @At("RETURN"))
	private void onHeal(float amount, CallbackInfo callback) {
		DeathBackModTriggers.fireHealed((LivingEntity) (Object) this, amount);
	}

	@Inject(method = "causeFallDamage", at = @At("HEAD"))
	private void onFall(float distance, float multiplier, DamageSource source, CallbackInfoReturnable<Boolean> callback) {
		DeathBackModTriggers.fireFalls((LivingEntity) (Object) this, distance, multiplier, source);
	}

	@Inject(method = "jumpFromGround", at = @At("HEAD"))
	private void onJump(CallbackInfo callback) {
		DeathBackModTriggers.fireJumps((LivingEntity) (Object) this);
	}

	@Inject(method = "hurt", at = @At("HEAD"))
	private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callback) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self.isDamageSourceBlocked(source))
			DeathBackModTriggers.fireShieldBlock(self, source, amount);
	}

	@Inject(method = "setItemInHand", at = @At("HEAD"))
	private void onSetItemInHand(net.minecraft.world.InteractionHand hand, ItemStack stack, CallbackInfo callback) {
		if (hand == net.minecraft.world.InteractionHand.OFF_HAND) {
			LivingEntity self = (LivingEntity) (Object) this;
			DeathBackModTriggers.fireSwitchHand(self, self.getMainHandItem(), stack);
		}
	}

	@Inject(method = "startUsingItem", at = @At("HEAD"))
	private void onStartUsing(net.minecraft.world.InteractionHand hand, CallbackInfo callback) {
		LivingEntity self = (LivingEntity) (Object) this;
		DeathBackModTriggers.fireUseItemStart(self, self.getItemInHand(hand), self.getUseItemRemainingTicks());
	}

	@Inject(method = "stopUsingItem", at = @At("HEAD"))
	private void onStopUsing(CallbackInfo callback) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (!self.getUseItem().isEmpty())
			DeathBackModTriggers.fireUseItemStop(self, self.getUseItem(), self.getUseItemRemainingTicks());
	}

	@Inject(method = "completeUsingItem", at = @At("HEAD"))
	private void onCompleteUsing(CallbackInfo callback) {
		LivingEntity self = (LivingEntity) (Object) this;
		DeathBackModTriggers.fireUseItemFinish(self, self.getUseItem(), self.getUseItemRemainingTicks());
	}

	@Inject(method = "dropExperience", at = @At("HEAD"))
	private void onDropExperience(net.minecraft.world.entity.Entity source, CallbackInfo callback) {
		LivingEntity self = (LivingEntity) (Object) this;
		DeathBackModTriggers.fireDropXp(self, 0, 0, source);
	}

	@Inject(method = "checkTotemDeathProtection", at = @At("RETURN"))
	private void onTotem(DamageSource source, CallbackInfoReturnable<Boolean> callback) {
		if (callback.getReturnValueZ())
			DeathBackModTriggers.fireUseTotem((LivingEntity) (Object) this, source, ((LivingEntity) (Object) this).getOffhandItem());
	}
}