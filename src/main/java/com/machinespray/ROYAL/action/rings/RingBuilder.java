package com.machinespray.ROYAL.action.rings;

import com.machinespray.ROYAL.NetHackItem;
import com.machinespray.ROYAL.action.ItemGroupBuilder;

public class RingBuilder implements ItemGroupBuilder {
	@Override
	public NetHackItem build(String s) {
		return new ItemRing(s);
	}
}
