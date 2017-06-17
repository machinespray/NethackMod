package com.machinespray.ROYAL.items.potion;

import com.machinespray.ROYAL.entity.EntityPotionRoyal;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class PotionEnlightenment extends PotionBase {

    public PotionEnlightenment() {
        super("enlightenment");
    }

    @Override
    void doDrinkAction(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (!worldIn.isRemote) {
            int i = 100;
            while (i > 0) {
                int j = EntityXPOrb.getXPSplit(i);
                i -= j;
                worldIn.spawnEntity(new EntityXPOrb(worldIn, entityLiving.posX, entityLiving.posY, entityLiving.posZ, j));
            }
        }
    }

    @Override
    public void onHit(RayTraceResult result, World world, EntityPotionRoyal entity) {
        world.playEvent(2002, new BlockPos(entity), PotionUtils.getPotionColor(PotionTypes.LEAPING));
        int i = 100;

        while (i > 0) {
            int j = EntityXPOrb.getXPSplit(i);
            i -= j;
            world.spawnEntity(new EntityXPOrb(world, entity.posX, entity.posY, entity.posZ, j));
        }

        entity.setDead();
    }

    public String getUse() {
        return "enlightenment";
    }

    public boolean hasUse() {
        return true;
    }
}
