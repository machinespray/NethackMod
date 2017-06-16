package com.machinespray.ROYAL.blocks;

import java.util.ArrayList;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.entity.TileFountain;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RoyalBlocks {
    public static ArrayList<NetHackAltar> altars = new ArrayList<NetHackAltar>();
    public static BlockFountain fountain;

    public static void initBlocks() {
        for (int i = 0; i < 3; i++)
            altars.add(new NetHackAltar(i));
        fountain = new BlockFountain();
    }

    public static void registerBlocks() {
        for (int i = 0; i < altars.size(); i++)
            altars.get(i).register();
        GameRegistry.register(fountain);
        GameRegistry.register(new ItemBlock(fountain).setRegistryName(fountain.getRegistryName()));
        GameRegistry.registerTileEntity(TileFountain.class, Main.MODID + "_fountain");
    }

    @SideOnly(Side.CLIENT)
    public static void registerClient() {
        for (int i = 0; i < altars.size(); i++)
            altars.get(i).registerClient();
    }
}
