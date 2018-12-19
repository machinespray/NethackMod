package com.machinespray.ROYAL;

import com.machinespray.ROYAL.action.ItemBase;
import com.machinespray.ROYAL.action.RandomizedItemGroup;
import com.machinespray.ROYAL.action.rings.RingBuilder;
import com.machinespray.ROYAL.action.rings.RingName;
import com.machinespray.ROYAL.action.scrolls.ScrollBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RoyalItems {
	public static RandomizedItemGroup scrolls = new RandomizedItemGroup(new ScrollBuilder(), Constants.scrollNames);
	public static RandomizedItemGroup rings = new RandomizedItemGroup(new RingBuilder(), RingName.getRingNames(), "R");
	public static NetHackItem base;

	public static void initItems() {
		scrolls.initNames();
		rings.initNames();
		base = new ItemBase("base");
	}

	public static void registerItems() {
		scrolls.register();
		rings.register();
		base.register();
	}

	@SideOnly(Side.CLIENT)
	public static void registerClient() {
		base.registerClient();
		scrolls.registerClient();
		rings.registerClient();
	}
}
