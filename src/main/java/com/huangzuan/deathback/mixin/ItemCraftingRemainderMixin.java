package com.huangzuan.deathback.mixin;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;

import net.minecraft.world.item.Item;

import com.huangzuan.deathback.item.CraftingRemainderSetter;

@Mixin(Item.class)
public abstract class ItemCraftingRemainderMixin implements CraftingRemainderSetter {
	@Shadow
	@Final
	@Mutable
	private Item craftingRemainingItem;

	@Override
	public void mcreatorSetCraftingRemainder(Item item) {
		this.craftingRemainingItem = item;
	}
}