package com.machinespray.ROYAL.action;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.NetHackItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class ItemBase extends NetHackItem {

	public ItemBase(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!(entityIn instanceof EntityPlayer) || worldIn.isRemote)
			return;
		EntityPlayer entityPlayer = (EntityPlayer) entityIn;
		entityPlayer.inventory.setInventorySlotContents(itemSlot, Main.getStackForWorld());
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		entityItem.setItem(Main.getStackForWorld());
		return false;
	}


}
