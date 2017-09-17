package com.machinespray.ROYAL.randomized.ring;

import com.machinespray.ROYAL.randomized.RandomAction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class RingAction extends RandomAction {
    public RingAction(RingActionGroup group, String name) {
        super(group, name);
        group.values().add(this);
    }

    public abstract void onWornTick(ItemStack itemstack,
                                    EntityLivingBase player);
}
