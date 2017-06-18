package com.machinespray.ROYAL.items;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.items.randomized.IRandomizedClass;
import com.machinespray.ROYAL.sync.knowledge.IKnowledgeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class NetHackItem extends Item implements Constants {
    public NetHackItem(String unlocalizedName) {
        this.setUnlocalizedName(unlocalizedName);
        this.setRegistryName("royal", unlocalizedName);
    }

    public static String id(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        nbt.setBoolean(BUCI, true);
        stack.setTagCompound(nbt);
        return nbt.getString(BUC);
    }

    private String uppercase(String s) {
        s = s.toLowerCase();
        for (int i = 0; i < s.length(); i++)
            if (s.toCharArray()[i] == ' ') {
                s = s.substring(0, i + 1) + s.toUpperCase().substring(i + 1, i + 2) + s.substring(i + 2).toLowerCase();
            }
        return s.toUpperCase().substring(0, 1) + s.substring(1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null)
            if (Main.getHandler(player) != null) {
                if (stack.getItem() instanceof IRandomizedClass) {
                    IRandomizedClass item = (IRandomizedClass) stack.getItem();
                    if (item.hasUse())
                        if (Main.getHandler(player).hasKnowledge(item.getUse().replace("_", " ")) || player.isCreative())
                            return uppercase(item.type()) + " of " + uppercase(item.getUse().replace("_", " "));
                }
            }
        return super.getItemStackDisplayName(stack);
    }

    public void register() {
        GameRegistry.register(this);
    }

    @SideOnly(Side.CLIENT)
    public void registerClient() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        IKnowledgeHandler kh = Main.getHandler(playerIn);
        IRandomizedClass nhi = null;
        if (nhi instanceof IRandomizedClass)
            nhi = ((IRandomizedClass) stack.getItem());

        if (nhi != null)
            if (nhi.hasUse())
                if (playerIn.isCreative() || kh.hasKnowledge(nhi.getUse()))
                    tooltip.add(super.getItemStackDisplayName(stack));
        if (stack.getTagCompound() != null) {
            if (stack.getTagCompound().getString(BUC) != "") {
                if (stack.getTagCompound().getBoolean(BUCI) || playerIn.isCreative())
                    tooltip.add(stack.getTagCompound().getString(BUC));
            } else {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setString(BUC, UNCURSED);
                stack.setTagCompound(nbt);
            }
        } else {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString(BUC, UNCURSED);
            stack.setTagCompound(nbt);
        }
    }

}
