package com.machinespray.ROYAL.scrolls;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public abstract class ScrollAction {
	public String name;
	public int id;

	public ScrollAction(String name) {
		this.name = name;
	}

	public abstract void onItemRightClick(World worldIn,
			EntityPlayer playerIn, EnumHand handIn);
}
