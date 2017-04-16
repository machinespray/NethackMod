package com.machinespray.ROYAL.proxy;

import com.machinespray.ROYAL.RoyalItems;

public class ClientProxy extends CommonProxy {
	@Override
	public void preinit(){
		super.preinit();
		RoyalItems.registerClient();
	}
	@Override
	public  void init(){
		super.init();
	}
	@Override
	public void postinit(){
		super.postinit();
	}

}
