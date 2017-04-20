package com.machinespray.ROYAL.scrolls;

import java.util.ArrayList;
import java.util.List;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.NetHackItem;
import com.machinespray.ROYAL.RoyalItems;
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
		if (ScrollActions.getAction(getUnlocalizedName()) != null)
			ScrollActions.getAction(getUnlocalizedName()).onItemRightClick(worldIn, playerIn, handIn);
		 return super.onItemRightClick(worldIn, playerIn, handIn);
    }
	@Override
	public boolean hasUse(){
		return Main.proxy.getScrollUse(this)!=null;
	}
	@Override
	public String getUse(){
		return Main.proxy.getScrollUse(this);
	}

	@Override
	public int getID(){
		for(int i=0;i<scrollNames.length;i++){
			String temp = getUnlocalizedName().split("\\.")[1].replace("_", " ");
			if(scrollNames[i].equals(temp))
				return i;
		}
		return super.getID();
		
	}

	@Override
	public String type() {
		return "scroll";
	}

}
