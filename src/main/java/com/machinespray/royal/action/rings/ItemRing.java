package com.machinespray.royal.action.rings;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.machinespray.royal.Constants;
import com.machinespray.royal.Main;
import com.machinespray.royal.NetHackItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemRing extends NetHackItem implements Constants, IBauble {

	public ItemRing(String unlocalizedName) {
		super(unlocalizedName);
		this.setCreativeTab(Main.royalTab);
		this.setMaxStackSize(1);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if (RingAction.getAction(getUnlocalizedName()) == null)
			return;
		if (player.world.isRemote) {
			RingAction.getAction(getUnlocalizedName()).clientAction(player);
			return;
		}
		if (RingAction.getAction(getUnlocalizedName()) != null)
			RingAction.getAction(getUnlocalizedName()).onWornTick(player);
	}

	@Override
	public boolean hasUse() {
		return Main.proxy.getRingUse(this) != null;
	}

	@Override
	public String getUse() {
		return Main.proxy.getRingUse(this);
	}

	@Override
	public String type() {
		return "ring";
	}

	@Override
	public int getID() {
		for (int i = 0; i < RingName.getRingNames().length; i++) {
			String temp = getUnlocalizedName().split("\\.")[1].replace("_", " ");
			temp = temp.substring(0, temp.length() - 1);
			if (RingName.getRingNames()[i].equals(temp))
				return i;
		}
		return super.getID();
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		//Reset Step Height for the levitation ring
		player.stepHeight = 0.06F;
	}


}
