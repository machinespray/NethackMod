package com.machinespray.ROYAL.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class RoyalConfig {
	private final Configuration config;
	public final boolean doMobDrops;

	public RoyalConfig(File location) {
		config = new Configuration(location);
		String SERVER = "SERVER";
		doMobDrops = config.get(SERVER, "DoMobDrops", true,"PackDev/Configurator should add another method of obtaining relics if this is disabled")
				.getBoolean();
		config.save();
	}
}
