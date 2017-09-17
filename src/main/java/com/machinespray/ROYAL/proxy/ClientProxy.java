package com.machinespray.ROYAL.proxy;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.NetHackItem;
import com.machinespray.ROYAL.RoyalItems;
import com.machinespray.ROYAL.altars.RoyalBlocks;
import com.machinespray.ROYAL.rings.ItemRing;
import com.machinespray.ROYAL.scrolls.ItemScroll;

public class ClientProxy extends CommonProxy {
	@Override
	public void preinit(){
		super.preinit();
		//RoyalItems.registerClient();
		//RoyalBlocks.registerClient();
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
	public String getUse(NetHackItem item) {
		try {
			if (item.getID() != -2)
				if (item.actionGroup != null)
					return item.actionGroup.getAction(item.getUnlocalizedName()).getKnowledgeName();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
