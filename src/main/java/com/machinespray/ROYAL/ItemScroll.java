package com.machinespray.ROYAL;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemScroll extends NetHackItem implements Constants {

	public ItemScroll(String unlocalizedName) {
		super(unlocalizedName);
		this.setCreativeTab(Main.royalTab);
	}

	public static void initNames() {
		for (String s : scrollNames) {
			s=s.replace(" ", "_");
			RoyalItems.scrolls.add(new ItemScroll(s));
			
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerClient() {
		ModelLoader.setCustomModelResourceLocation(this,0,new ModelResourceLocation(RoyalItems.base.getRegistryName(), "inventory"));
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		worldIn.getWorldInfo().getSeed();
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
