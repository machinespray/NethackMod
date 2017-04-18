package com.machinespray.ROYAL.rings;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public abstract class RingAction {
	public String name;	
	public int id;
	public RingAction(String name){
		this.name=name;
	}
	public abstract void onWornTick(ItemStack itemstack, EntityLivingBase player);

}
