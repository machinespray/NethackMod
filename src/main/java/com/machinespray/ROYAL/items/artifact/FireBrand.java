package com.machinespray.ROYAL.items.artifact;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class FireBrand extends ArtifactBase {
    public FireBrand() {
        super("firebrand", 3.0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        tooltip.add(TextFormatting.DARK_PURPLE.toString() + "Extra Effective against Overworld Animals");
        tooltip.add(TextFormatting.DARK_PURPLE.toString() + "Gain Fire Resistance");
        tooltip.add(TextFormatting.DARK_PURPLE.toString() + "Becomes Stronger with Fire Aspect Enchant");
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote)
            if (isSelected && entityIn instanceof EntityPlayerMP)
                ((EntityPlayerMP) entityIn).addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 61, 0, false, false));

    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        double temp = 0;
        temp += damageBase;
        if (target instanceof EntityAnimal) {
            temp *= 2;
            target.setFire(10);
        }
        if (stack.isItemEnchanted()) {
            NBTTagList list = stack.getEnchantmentTagList();
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound ench = (NBTTagCompound) list.get(i);
                if (ench.getInteger("id") == 20 && !target.isImmuneToFire())
                    temp *= 2;
            }
        }
        target.attackEntityFrom(DamageSource.causeMobDamage(attacker), (float) (temp));

        return super.hitEntity(stack, target, attacker);
    }
}
