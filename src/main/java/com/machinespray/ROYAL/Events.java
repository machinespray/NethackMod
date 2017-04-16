package com.machinespray.ROYAL;

import com.machinespray.ROYAL.rings.RingActions;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Events {
	@SubscribeEvent
	public void onLoad(WorldEvent.Load e) {
		RingActions.match(e.getWorld().getSeed());
	}
}
