package com.machinespray.ROYAL.proxy;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.machinespray.ROYAL.RoyalItems;
import com.machinespray.ROYAL.rings.RingActions;

public class CommonProxy {
	
	public void preinit(){
		RoyalItems.initItems();
		RoyalItems.registerItems();
	}
	public  void init(){
		GameRegistry.addShapelessRecipe(new ItemStack(RoyalItems.scrolls.get(0)), Items.PAPER);
		RingActions.initActions();
	}
	public void postinit(){
	}

}
