package com.machinespray.ROYAL.proxy;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.machinespray.ROYAL.RoyalItems;

public class CommonProxy {
	
	public void preinit(){}
	public  void init(){
	RoyalItems.initItems();
	GameRegistry.addShapelessRecipe(new ItemStack(RoyalItems.test), Items.PAPER);
	}
	public void postinit(){
	RoyalItems.registerItems();
	}

}
