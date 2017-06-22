package com.machinespray.ROYAL.items.artifact;

import com.google.common.collect.Multimap;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.items.NetHackItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public abstract class ArtifactBase extends NetHackItem {
    public static ArrayList<ArtifactBase> artifacts = new ArrayList<ArtifactBase>();
    public double damageBase;

    public ArtifactBase(String name, double damageBase) {
        super(name);
        this.damageBase = damageBase;
        this.setCreativeTab(Main.royalTab);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setMaxDamage(2343);
        artifacts.add(this);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote)
            if (isIntelligent())
                if (shouldBlast(entityIn)) {
                    EntityLightningBolt lb = new EntityLightningBolt(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ, true);
                    worldIn.addWeatherEffect(lb);
                    EntityItem artifact = new EntityItem(worldIn);
                    artifact.setPosition(entityIn.posX,entityIn.posY,entityIn.posZ);
                    artifact.setPickupDelay(60);
                    artifact.setEntityItemStack(stack.splitStack(1));
                    worldIn.spawnEntity(artifact);
                    if(entityIn instanceof EntityLivingBase){
                        EntityLivingBase entityLivingBase = (EntityLivingBase) entityIn;
                        entityLivingBase.attackEntityFrom(DamageSource.MAGIC,6.0F);
                        entityLivingBase.addPotionEffect(new PotionEffect(Potion.getPotionById(15),20));
                    }
                }
    }

    protected boolean isIntelligent() {
        return false;
    }

    protected boolean shouldBlast(Entity p) {
        return false;
    }

    public static AttributeModifier getNewAttackModifier(double damage) {
        return new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", damage, 0);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        Block block = state.getBlock();

        if (block == Blocks.WEB) {
            return 15.0F;
        } else {
            Material material = state.getMaterial();
            return material != Material.PLANTS && material != Material.VINE && material != Material.CORAL && material != Material.LEAVES && material != Material.GOURD ? 1.0F : 1.5F;
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", damageBase, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }

        return multimap;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}