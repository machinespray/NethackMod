package com.machinespray.ROYAL.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.RoyalItems;
import com.machinespray.ROYAL.randomized.ring.RingActionGroup;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemRing extends NetHackItem implements Constants, IBauble {

	public ItemRing(String unlocalizedName) {
		super(unlocalizedName);
		actionGroup=RingActionGroup.INSTANCE;
		this.setCreativeTab(Main.royalTab);
	}

	public static void initNames() {
		for (String s : ringNames) {
			s = s.replace(" ", "_");
			RoyalItems.rings.add(new ItemRing(s+"R"));

		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}
	
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if(!player.world.isRemote)
		if (actionGroup.getAction(getUnlocalizedName()) != null)
			RingActionGroup.INSTANCE.getAction(getUnlocalizedName()).onWornTick(itemstack,
					player);
	}

	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		if (itemstack.getTagCompound() != null)
			if (itemstack.getTagCompound().getString(BUC) != null){}
				//Do Negative removal effect
		return true;
	}
	@Override
	public boolean hasUse(){
		return Main.proxy.getUse(this)!=null;
	}
	@Override
	public String getUse(){
		return Main.proxy.getUse(this);
	}

	@Override
	public String type() {
		return "ring";
	}
	@Override
	public int getID(){
		for(int i=0;i<ringNames.length;i++){
			String temp = getUnlocalizedName().split("\\.")[1].replace("_", " ");
			temp = temp.substring(0, temp.length()-1);
			if(ringNames[i].equals(temp))
				return i;
		}
		return super.getID();
		
	}

	}
