package com.machinespray.ROYAL.items.potion;

import com.machinespray.ROYAL.entity.EntityPotionRoyal;
import com.machinespray.ROYAL.items.NetHackItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class PotionBase extends NetHackItem {

    public PotionBase(String unlocalizedName) {
        this.setUnlocalizedName("potion_" + unlocalizedName);
        this.setRegistryName("royal", "potion_" + unlocalizedName);
    }


    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        EntityPlayer entityplayer = entityLiving instanceof EntityPlayer ? (EntityPlayer) entityLiving : null;

        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
            stack.shrink(1);
        }
        doDrinkAction(stack, worldIn, entityLiving);
        return stack;
    }

    @Override
    public String type() {
        return "potion";
    }

    abstract void doDrinkAction(ItemStack stack, World worldIn, EntityLivingBase entityLiving);

    public abstract void onHit(RayTraceResult result, World world, EntityPotionRoyal entity);

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (handIn.equals(EnumHand.OFF_HAND) && !(playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof NetHackItem)) {
            ItemStack itemstack1 = playerIn.capabilities.isCreativeMode ? itemstack.copy() : itemstack.splitStack(1);
            worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!worldIn.isRemote) {
                EntityPotionRoyal entitypotion = new EntityPotionRoyal(worldIn, playerIn, this);
                entitypotion.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.5F, 1.0F);
                worldIn.spawnEntity(entitypotion);
            }

            playerIn.addStat(StatList.getObjectUseStats(this));
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        } else if (handIn.equals(EnumHand.MAIN_HAND)) {
            playerIn.setActiveHand(handIn);
            return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }
}
