package com.machinespray.royal.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class RoyalConfig {
	public final boolean doMobDrops;
	public final int scrollParts;
	public final int ringParts;
	private final Configuration config;

	public RoyalConfig(File location) {
		config = new Configuration(location);
		String SERVER = "SERVER";
		doMobDrops = config.getBoolean("DoMobDrops", SERVER, true, "PackDev/Configurator should add another method of obtaining relics if this is disabled");
		scrollParts = config.getInt("scrollParts", SERVER, 1, 0, 500, "Compared to ringParts to find the relative chance of getting a ring or scroll");
		ringParts = config.getInt("ringParts", SERVER, 1, 0, 500, "Compared to scrollParts to find the relative chance of getting a ring or scroll");
		config.save();
	}
}
