package com.machinespray.royal.action.scrolls;

import com.machinespray.royal.NetHackItem;
import com.machinespray.royal.action.ItemGroupBuilder;

public class ScrollBuilder implements ItemGroupBuilder {
	@Override
	public NetHackItem build(String s) {
		return new ItemScroll(s);
	}
}
