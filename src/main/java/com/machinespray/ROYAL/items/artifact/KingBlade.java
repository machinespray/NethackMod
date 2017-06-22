package com.machinespray.ROYAL.items.artifact;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.machinespray.ROYAL.Values;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class KingBlade extends ArtifactBase {

    public KingBlade() {
        super("kingblade", 3.5);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        tooltip.add(TextFormatting.DARK_PURPLE.toString() + "Gain experience for extra damage");
        tooltip.add(TextFormatting.DARK_PURPLE.toString() + "Lose experience when hit");
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        HashMultimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        return multimap;
    }


    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack,worldIn,entityIn,itemSlot,isSelected);
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if (player.hurtResistantTime == 1 && !worldIn.isRemote)
                player.removeExperienceLevel(Values.random.nextInt(player.experienceLevel / 2));
            double newDamage = damageBase * (1 + (((double) Math.min(30, player.experienceLevel)) / 17.5));
            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null)
                tag = new NBTTagCompound();
            if (newDamage != tag.getDouble("currentDamage")) {
                NBTTagList list = new NBTTagList();
                AttributeModifier modifier = new AttributeModifier("Experience-Based Damage", newDamage, 0);
                NBTTagCompound tagModifier = SharedMonsterAttributes.writeAttributeModifierToNBT(modifier);
                tagModifier.setString("Slot", "mainhand");
                tagModifier.setString("AttributeName", SharedMonsterAttributes.ATTACK_DAMAGE.getName());
                list.appendTag(tagModifier);
                modifier = new AttributeModifier("Weapon modifier", -2.4000000953674316D, 0);
                tagModifier = SharedMonsterAttributes.writeAttributeModifierToNBT(modifier);
                tagModifier.setString("Slot", "mainhand");
                tagModifier.setString("AttributeName", SharedMonsterAttributes.ATTACK_SPEED.getName());
                list.appendTag(tagModifier);
                tag.setTag("AttributeModifiers", list);
                tag.setDouble("currentDamage", newDamage);
                stack.setTagCompound(tag);
            }
        }
    }


}
