package com.machinespray.ROYAL.action;

import com.machinespray.ROYAL.NetHackItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class RandomizedItemGroup {
	ItemGroupBuilder builder;
	String[] names;
	String suffix;
	ArrayList<NetHackItem> items = new ArrayList<>();


	public RandomizedItemGroup(ItemGroupBuilder builder, String[] names) {
		this(builder, names, "");
	}

	public RandomizedItemGroup(ItemGroupBuilder builder, String[] names, String suffix) {
		this.builder = builder;
		this.names = names;
		this.suffix = suffix;
	}

	public void initNames() {
		for (String s : names) {
			s = s.replace(" ", "_");
			items.add(builder.build(s+suffix));

		}
	}

	public void register() {
		for (NetHackItem i : items)
			i.register();
	}

	@SideOnly(Side.CLIENT)
	public void registerClient() {
		for (NetHackItem i : items)
			i.registerClient();
	}
}
