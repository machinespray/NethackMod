package com.machinespray.ROYAL;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.core.config.plugins.ResolverUtil.Test;

public class RoyalItems {
	public static NetHackItem test;
	
	public static void initItems(){
		test = new NetHackItem("test");
	}

	public static void registerItems() {
		test.register();
	}
	@SideOnly(Side.CLIENT)
	public static void registerClient(){
		test.registerClient();
	}
}
