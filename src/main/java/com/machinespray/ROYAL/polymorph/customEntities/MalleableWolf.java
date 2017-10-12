package com.machinespray.ROYAL.polymorph.customEntities;

import net.minecraft.entity.passive.EntityWolf;

public class MalleableWolf extends EntityWolf {

    public MalleableWolf() {
        super(null);
    }

    @Override
    public boolean isAngry() {
        return true;
    }
}
