package com.machinespray.ROYAL.rings;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.NetHackItem;
import com.machinespray.ROYAL.RoyalItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemRing extends NetHackItem implements Constants, IBauble {

	public ItemRing(String unlocalizedName) {
		super(unlocalizedName);
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
		if (RingAction.getAction(getUnlocalizedName()) != null)
			RingAction.getAction(getUnlocalizedName()).onWornTick(player);
	}

	@Override
	public boolean hasUse(){
		return Main.proxy.getRingUse(this)!=null;
	}
	@Override
	public String getUse(){
		return Main.proxy.getRingUse(this);
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
