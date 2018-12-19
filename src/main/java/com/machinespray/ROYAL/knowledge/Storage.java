package com.machinespray.ROYAL.knowledge;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class Storage implements Capability.IStorage<IKnowledgeHandler> {

	@Override
	public NBTBase writeNBT(Capability<IKnowledgeHandler> capability,
							IKnowledgeHandler instance, EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("length", instance.getKnowledge().size());
		for (int i = 0; i < instance.getKnowledge().size(); i++) {
			nbt.setString(String.valueOf(i), instance.getKnowledge().get(i));
		}
		return nbt;
	}

	@Override
	public void readNBT(Capability<IKnowledgeHandler> capability,
						IKnowledgeHandler instance, EnumFacing side, NBTBase nbtB) {
		NBTTagCompound nbt = (NBTTagCompound) nbtB;
		int length = nbt.getInteger("length");
		for (int i = 0; i < length; i++)
			instance.addKnowledge(nbt.getString(String.valueOf(i)));

	}

}
