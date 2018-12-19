package com.machinespray.ROYAL.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ClientProxy extends CommonProxy {
	@Override
	public void preinit() {
		super.preinit();
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void postinit() {
		super.postinit();
	}

	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}
}
