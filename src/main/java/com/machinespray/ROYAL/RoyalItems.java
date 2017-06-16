package com.machinespray.ROYAL;

import java.util.ArrayList;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.machinespray.ROYAL.rings.ItemRing;
import com.machinespray.ROYAL.scrolls.ItemScroll;

public class RoyalItems {
	public static ArrayList<ItemScroll> scrolls = new ArrayList<ItemScroll>();
	public static NetHackItem base;
	public static ArrayList<ItemRing> rings = new ArrayList<ItemRing>();
	public static NetHackItem AmuletUndying;

	public static void initItems() {
		ItemScroll.initNames();
		ItemRing.initNames();
		base = new NetHackItem("base");
		//AmuletUndying = new NetHackItem("amuletOfUndying");
	}

	public static void registerItems() {
		for (ItemScroll i : scrolls)
			i.register();
		for (ItemRing i : rings)
			i.register();
		base.register();
		//AmuletUndying.register();

	}

	@SideOnly(Side.CLIENT)
	public static void registerClient() {
		for (ItemScroll i : scrolls)
			i.registerClient();
		for (ItemRing i : rings)
			i.registerClient();
		base.registerClient();
		//AmuletUndying.registerClient();
	}
}
