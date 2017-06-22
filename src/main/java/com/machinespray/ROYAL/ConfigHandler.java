package com.machinespray.ROYAL;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
    public static Configuration config;
    public static boolean DoCustomArmorRender;
    public static boolean DoCustomArmRender;


    public static void init() {
        config.addCustomCategoryComment("Client-Side Options", "Affects rendering, usually weird ones that may affect other mods");
        DoCustomArmorRender = config.getBoolean("Do Custom Armor Render", "Client-Side Options", true, "Determines whether or not to render the player's raw armor attribute(true),or to render as default(false)");
        DoCustomArmRender = config.getBoolean("Do Custom First Person Arm", "Client-Side Options", true, "Determines whether or not ROYAL attempts to replace the player's skin for their arm in first person");
        config.save();
    }
}
