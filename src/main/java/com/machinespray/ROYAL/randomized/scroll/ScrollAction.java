package com.machinespray.ROYAL.randomized.scroll;

import com.machinespray.ROYAL.randomized.RandomAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public abstract class ScrollAction extends RandomAction {
    public ScrollAction(ScrollActionGroup group,String name) {
        super(group,name);
        group.values.add(this);
    }

    public abstract void onItemRightClick(World worldIn,
                                          EntityPlayer playerIn, EnumHand handIn);
}
