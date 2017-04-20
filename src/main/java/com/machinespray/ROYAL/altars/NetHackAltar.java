package com.machinespray.ROYAL.altars;

import com.machinespray.ROYAL.Main;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetHackAltar extends Block {
	private ItemBlock item;
	private int type;
	public NetHackAltar(int type) {
		super(Material.IRON);
		this.setCreativeTab(Main.royalTab);
		switch (type) {
		case 0:
			setUnlocalizedName("Lawful");
			break;
		case 1:
			setUnlocalizedName("Neutral");
			break;
		case 2:
			setUnlocalizedName("Chaotic");
			break;
		}
		this.setRegistryName("royal", getUnlocalizedName()+"Altar");
	}
	public void register(){
		GameRegistry.register(this);
		item = new ItemBlock(this);
		item.setRegistryName(new ResourceLocation("royal",this.getUnlocalizedName()+"Altar"));
		GameRegistry.register(item);
	}
	public int getType() {
		return type;
	}
	@SideOnly(Side.CLIENT)
	public void registerClient() {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(),"inventory"));
	}

}
