package com.machinespray.royal.action.rings;

import com.machinespray.royal.NetHackItem;
import com.machinespray.royal.action.ItemGroupBuilder;

public class RingBuilder implements ItemGroupBuilder {
	@Override
	public NetHackItem build(String s) {
		return new ItemRing(s);
	}
}
