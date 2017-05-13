package com.machinespray.ROYAL.blocks;

import java.util.ArrayList;

import com.machinespray.ROYAL.blocks.altars.NetHackAltar;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RoyalBlocks {
	public static ArrayList<NetHackAltar> altars = new ArrayList<NetHackAltar>();
	public static void initBlocks(){
		for(int i=0;i<3;i++)
			altars.add(new NetHackAltar(i));
	}
	public static void registerBlocks(){
		for(int i=0;i<altars.size();i++)
			altars.get(i).register();
	}
	@SideOnly(Side.CLIENT)
	public static void registerClient(){
		for(int i=0;i<altars.size();i++)
			altars.get(i).registerClient();
	}
}
