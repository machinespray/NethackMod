package com.machinespray.ROYAL.proxy;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.rings.ItemRing;
import com.machinespray.ROYAL.scrolls.ItemScroll;

public class ClientProxy extends CommonProxy {
	@Override
	public void preinit(){
		super.preinit();
	}
	@Override
	public  void init(){
		super.init();
		
	}
	@Override
	public void postinit(){
		super.postinit();
	}
	@Override
	public String getRingUse(ItemRing ring) {
		if(ring.getID()!=-2)
			if(Main.scrolls!=null)
		return Main.rings[ring.getID()];
		return null;
	}
	@Override
	public String getScrollUse(ItemScroll scroll) {
		if(scroll.getID()!=-2)
			if(Main.scrolls!=null)
		return Main.scrolls[scroll.getID()];
		return null;
	}

}
