package com.machinespray.ROYAL.action.rings;

import com.machinespray.ROYAL.NetHackItem;

import static com.machinespray.ROYAL.action.rings.RingName.Prefix.*;

public enum RingName {
	PEARL, IRON(INGOT), TWISTED, STEEL(INGOT), WIRE(IRON),
	DIAMOND(GEM), SHINY(IRON), BRONZE(INGOT), BRASS(INGOT), COPPER(INGOT), SILVER(INGOT),
	GOLD(INGOT), WOODEN(STICK, "wood"), GRANITE(STONE), OPAL, CLAY(INGOT, "brick"), CORAL, BLACK_ONYX,
	MOONSTONE, TIGER_EYE, JADE, AGATE, TOPAZ, SAPPHIRE,
	RUBY, ENGAGEMENT(DIAMOND), QUARTZ(GEM), EMERALD(GEM);

	private String prefix;
	private String copy = null;
	RingName() {
		prefix = "";
		copy = "";
	}

	RingName(Prefix prefix) {
		this.prefix = prefix.toString();
	}

	RingName(RingName copy) {
		this.prefix = copy.getPrefix();
		this.copy = copy.toString();
	}

	RingName(Prefix prefix, String mimic) {
		this.prefix = prefix.toString();
		this.copy = mimic;
	}

	public static String[] getRingNames() {
		String[] names = new String[values().length];
		for (int i = 0; i < values().length; i++)
			names[i] = values()[i].toString().toLowerCase().replace("_", " ");
		return names;

	}

	public String getPrefix() {
		return prefix;
	}

	public String getOreDictName() {
		String s = copy == null ? getName() : copy;
		s = NetHackItem.uppercase(s);
		return prefix.toLowerCase() + s;
	}

	public String getName() {
		return toString();
	}

	public enum Prefix {GEM, INGOT, STONE, STICK}
}
