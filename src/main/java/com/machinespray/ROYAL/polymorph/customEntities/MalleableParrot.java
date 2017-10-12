package com.machinespray.ROYAL.polymorph.customEntities;

import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.world.World;

/**
 * Created by ModOff on 9/18/2017.
 */
public class MalleableParrot extends EntityParrot {

    public MalleableParrot() {
        super(null);
    }
    @Override
    public boolean isFlying(){
        return true;
    }
}
