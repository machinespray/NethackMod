package com.machinespray.ROYAL.polymorph.customEntities;

import net.minecraft.entity.passive.AbstractHorse;

public class MalleableHorse extends AbstractHorse {

    public MalleableHorse() {
        super(null);
    }

    @Override
    protected void initHorseChest() {
    }
    @Override
    public boolean isChild(){
        return false;
    }

}
