package com.machinespray.ROYAL.items.artifact;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class Grayswandir extends ArtifactBase {
    public Grayswandir() {
        super("grayswandir", 3.0);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (target.isEntityUndead()) {
            double temp = 1;
            temp += damageBase;
            if (stack.isItemEnchanted()) {
                NBTTagList list = stack.getEnchantmentTagList();
                for (int i = 0; i < list.tagCount(); i++) {
                    NBTTagCompound ench = (NBTTagCompound) list.get(i);
                    temp += Enchantment.getEnchantmentByID(ench.getInteger("id")).calcDamageByCreature(ench.getInteger("lvl"), target.getCreatureAttribute());
                }
            }
            target.attackEntityFrom(DamageSource.causeMobDamage(attacker), (float) (temp * 2));
        }
        return super.hitEntity(stack, target, attacker);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        tooltip.add(TextFormatting.DARK_PURPLE.toString() + "Double Damage to Undead");
    }

}