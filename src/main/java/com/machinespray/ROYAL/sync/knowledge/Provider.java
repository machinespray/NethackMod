package com.machinespray.ROYAL.sync.knowledge;

import com.machinespray.ROYAL.Main;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class Provider implements ICapabilitySerializable<NBTTagCompound> {

    IKnowledgeHandler instance = Main.CAPABILITY_KNOWLEDGE.getDefaultInstance();

    @Override
    public NBTTagCompound serializeNBT() {

        return (NBTTagCompound) Main.CAPABILITY_KNOWLEDGE.getStorage().writeNBT(Main.CAPABILITY_KNOWLEDGE, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

        Main.CAPABILITY_KNOWLEDGE.getStorage().readNBT(Main.CAPABILITY_KNOWLEDGE, instance, null, nbt);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == Main.CAPABILITY_KNOWLEDGE;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return hasCapability(capability, facing) ? Main.CAPABILITY_KNOWLEDGE.<T>cast(instance) : null;
    }
}