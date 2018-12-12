package com.machinespray.ROYAL.action.scrolls;

import com.machinespray.ROYAL.NetHackItem;
import com.machinespray.ROYAL.action.ItemGroupBuilder;

public class ScrollBuilder implements ItemGroupBuilder {
	@Override
	public NetHackItem build(String s) {
		return new ItemScroll(s);
	}
}
