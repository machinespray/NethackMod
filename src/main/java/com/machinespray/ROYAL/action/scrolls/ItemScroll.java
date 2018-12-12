package com.machinespray.ROYAL.action.scrolls;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.NetHackItem;
import com.machinespray.ROYAL.RoyalItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemScroll extends NetHackItem implements Constants {

	public ItemScroll(String unlocalizedName) {
		super(unlocalizedName);
		this.setCreativeTab(Main.royalTab);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void registerClient() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(RoyalItems.base.getRegistryName(), "inventory"));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote)
			if (ScrollAction.getAction(getUnlocalizedName()) != null && handIn.equals(EnumHand.MAIN_HAND))
				ScrollAction.getAction(getUnlocalizedName()).onItemRightClick(playerIn);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public boolean hasUse() {
		return Main.proxy.getScrollUse(this) != null;
	}

	@Override
	public String getUse() {
		return Main.proxy.getScrollUse(this);
	}

	@Override
	public int getID() {
		for (int i = 0; i < scrollNames.length; i++) {
			String temp = getUnlocalizedName().split("\\.")[1].replace("_", " ");
			if (scrollNames[i].equals(temp))
				return i;
		}
		return super.getID();

	}

	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return hasUse();
	}


	@Override
	public String type() {
		return "scroll";
	}

}
