package com.machinespray.ROYAL.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileFountain extends TileEntity {
    public static int sid = 0;
    public potionType contents = potionType.EMPTY;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        contents = potionType.fromId(compound.getTagId("id"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("id", contents.id);
        return compound;
    }

    public enum potionType {
        EMPTY, WATER, POLYMORPH;
        private int id;

        potionType() {
            id = sid;
            sid++;
        }

        public static potionType fromId(int id) {
            for (int i = 0; i < values().length; i++)
                if (values()[i].id == id)
                    return values()[i];
            return null;
        }
    }

    ;
}
