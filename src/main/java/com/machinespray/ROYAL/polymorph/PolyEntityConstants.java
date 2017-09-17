package com.machinespray.ROYAL.polymorph;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;

import java.util.ArrayList;

public enum PolyEntityConstants {
    ZOMBIE(EntityZombie.class),
    LARGE_ANIMALS(EntityCow.class, EntitySheep.class, EntityPig.class),
    SKELETON(EntitySkeleton.class, EntityWitherSkeleton.class);
    public ArrayList<Class<? extends EntityLivingBase>> contains = new ArrayList<Class<? extends EntityLivingBase>>();

    PolyEntityConstants(Class<? extends EntityLivingBase>... args) {
        for (int i = 0; i < args.length; i++) {
            contains.add(args[i]);
        }
    }

    public static PolyEntityConstants getGroup(EntityLivingBase e) {
        for (PolyEntityConstants c : PolyEntityConstants.values())
            for (Class C : c.contains)
                if (e.getClass().equals(C))
                    return c;
        return null;
    }

}
