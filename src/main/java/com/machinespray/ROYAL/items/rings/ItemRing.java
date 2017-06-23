package com.machinespray.ROYAL.items.rings;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.machinespray.ROYAL.items.randomized.DefaultRandomizedClass;
import com.machinespray.ROYAL.items.randomized.IRandomizedClass;
import com.machinespray.ROYAL.items.randomized.RandomObjectFactory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemRing extends DefaultRandomizedClass implements IBauble, IRandomizedClass {

    public ItemRing() {
        super("RING", ringNames, RingAction.values(), new RandomObjectFactory() {
            @Override
            public DefaultRandomizedClass create(String s, DefaultRandomizedClass parent) {
                return new ItemRing(s, parent);
            }
        });
    }

    public ItemRing(String name, DefaultRandomizedClass parent) {
        super(name, parent);
    }

    @Override
    public String[] initNames(String[] names) {
        String[] namesToReturn = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            String s = names[i];
            s = s.replace(" ", "_");
            namesToReturn[i] = s + "R";
        }
        return namesToReturn;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.RING;
    }

    //Do randomized action
    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (getUse() != null)
            ((RingAction) getActualUse()).onWornTick(itemstack,
                    player);
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        if (itemstack.getTagCompound() != null)
            if (itemstack.getTagCompound().getString(BUC) != null)
                return !itemstack.getTagCompound().getString(BUC)
                        .equals(CURSED) || ((EntityPlayer) player).isCreative();
        return true;
    }

}
