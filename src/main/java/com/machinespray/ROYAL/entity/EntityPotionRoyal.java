package com.machinespray.ROYAL.entity;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.items.potion.PotionBase;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityPotionRoyal extends EntityThrowable {
    private PotionBase potion;

    public EntityPotionRoyal(World worldIn) {
        super(worldIn);
    }

    public EntityPotionRoyal(World worldIn, EntityLivingBase throwerIn, PotionBase potion) {
        super(worldIn, throwerIn);
        this.potion = potion;
    }

    protected void onImpact(RayTraceResult result) {
        if (!world.isRemote) {
            try {
                potion.onHit(result, world, this);
            }catch(Exception e){
                System.out.println("Potion impact event was unloaded. Killing the entity.");
            }
            this.setDead();
        }
    }
}
