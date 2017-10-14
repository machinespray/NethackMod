package com.machinespray.ROYAL.randomized.ring;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import lilliputian.potions.PotionLilliputian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import java.util.List;

public class RingActions {
    public static void init(RingActionGroup group) {
        new RingAction(group, "AGGRAVATE_MONSTER") {

            @Override
            public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
                AxisAlignedBB box = new AxisAlignedBB(player.posX - 5,
                        player.posY - 5, player.posZ - 5, player.posX + 5,
                        player.posY + 5, player.posZ + 5);
            /*if (buc.equals(CURSED))
                box = box.expand(2,2,2);
            if (buc.equals(BLESSED))
                box = box.expand(-.5,-.5,-.5);*/
                List<EntityLiving> list = player.world.getEntitiesWithinAABB(
                        EntityLiving.class, box);
                for (EntityLiving e : list) {
                    if (e.getAttackTarget() == null
                            || e.getAttackTarget() != player) {
                        e.setAttackTarget(player);
                    }
                }
            }
        };
        new RingAction(group, "CONFLICT") {
            @Override
            public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
                AxisAlignedBB box = new AxisAlignedBB(player.posX - 5,
                        player.posY - 5, player.posZ - 5, player.posX + 5,
                        player.posY + 5, player.posZ + 5);
                List<EntityLiving> list = player.world.getEntitiesWithinAABB(
                        EntityLiving.class, box);
                for (EntityLiving e : list) {
                    if (e.getAttackTarget() == null) {
                        EntityLiving entity = list.get(Main.random.nextInt(list
                                .size()));
                        if (!entity.equals(e))
                            e.setAttackTarget(entity);
                    }

                }
            }
        };
        new RingAction(group, "LEVITATION") {
            @Override
            public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
                player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:levitation")
                        , 20, -1, false, false));
                player.addPotionEffect(new PotionEffect(
                        Potion.getPotionFromResourceLocation("minecraft:speed"), 20, 255, false, false));
                discover(player);
            }
        };
        new RingAction(group, "SEE_INVISIBLE") {

            @Override
            public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
                List<EntityLiving> list = player.world.getEntitiesWithinAABB(
                        EntityLiving.class, new AxisAlignedBB(player.posX - 5,
                                player.posY - 5, player.posZ - 5,
                                player.posX + 5, player.posY + 5,
                                player.posZ + 5));
                for (EntityLiving e : list) {
                    if (e.getActivePotionEffect(Potion.getPotionById(14)) != null)
                        e.addPotionEffect(new PotionEffect(Potion
                                .getPotionById(24), 20));
                }
            }
        };
        new RingAction(group, "TELEPORTATION") {
            @Override
            public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
                if (Main.random.nextInt(200) > 198)
                    if (!player.world.isRemote) {
                        double d0 = player.posX;
                        double d1 = player.posY;
                        double d2 = player.posZ;

                        for (int i = 0; i < 16; i++) {
                            double d3 = player.posX
                                    + (player.getRNG().nextDouble() - 0.5D)
                                    * 16.0D;
                            double d4 = MathHelper
                                    .clamp(player.posY
                                                    + (double) (player.getRNG()
                                                    .nextInt(16) - 8), 0.0D,
                                            (double) (player.world
                                                    .getActualHeight() - 1));
                            double d5 = player.posZ
                                    + (player.getRNG().nextDouble() - 0.5D)
                                    * 16.0D;

                            if (player.isRiding()) {
                                player.dismountRidingEntity();
                            }

                            if (player.attemptTeleport(d3, d4, d5)) {
                                player.world.playSound((EntityPlayer) null, d0,
                                        d1, d2,
                                        SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
                                        SoundCategory.PLAYERS, 1.0F, 1.0F);
                                player.playSound(
                                        SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
                                        1.0F, 1.0F);
                                IKnowledgeHandler knowledge = Main
                                        .getHandler(player);
                                if (!knowledge.hasKnowledge("teleportation")) {
                                    if (!player.world.isRemote)
                                        player.sendMessage(new TextComponentString(
                                                "You discover this is a ring of teleportation!"));
                                    knowledge.addKnowledge("teleportation");
                                }
                            }
                            break;

                        }
                    }
            }
        };
        new RingAction(group, "HUNGER") {
            @Override
            public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
                player.addPotionEffect(new PotionEffect(Potion
                        .getPotionById(17), 21, 0, false, false));
                IKnowledgeHandler knowledge = Main.getHandler(player);
                if (!knowledge.hasKnowledge("hunger")) {
                    if (!player.world.isRemote)
                        player.sendMessage(new TextComponentString(
                                "You discover this is a ring of hunger!"));
                    knowledge.addKnowledge("hunger");
                }
            }
        };
        new RingAction(group, "PROTECTION") {
            @Override
            public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
                player.addPotionEffect(new PotionEffect(Potion
                        .getPotionById(11), 21, 0, false, false));
                IKnowledgeHandler knowledge = Main.getHandler(player);
                if (!knowledge.hasKnowledge("protection")) {
                    if (!player.world.isRemote)
                        player.sendMessage(new TextComponentString(
                                "You discover this is a ring of protection!"));
                    knowledge.addKnowledge("protection");
                }
            }
        };
        new RingAction(group, "REGENERATION") {
            @Override
            public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
                if (player.getActivePotionEffect(Potion.getPotionById(10)) == null)
                    player.addPotionEffect(new PotionEffect(Potion
                            .getPotionById(10), 80, 0, false, false));
                IKnowledgeHandler knowledge = Main.getHandler(player);
                if (!knowledge.hasKnowledge("regeneration")) {
                    if (!player.world.isRemote)
                        player.sendMessage(new TextComponentString(
                                "You discover this is a ring of regeneration!"));
                    knowledge.addKnowledge("regeneration");
                }
            }
        };
        new RingAction(group, "FIRE_RESISTANCE") {
            @Override
            public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
                player.addPotionEffect(new PotionEffect(Potion
                        .getPotionById(12), 10, 0, false, false));
                IKnowledgeHandler knowledge = Main.getHandler(player);
                if (!knowledge.hasKnowledge("fire resistance")) {
                    if (!player.world.isRemote)
                        player.sendMessage(new TextComponentString(
                                "You discover this is a ring of fire resistance!"));
                    knowledge.addKnowledge("fire resistance");
                }
            }
        };
        new RingAction(group, "STRENGTH") {
            @Override
            public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
                player.addPotionEffect(new PotionEffect(
                        Potion.getPotionById(5), 10, 0, false, false));
                IKnowledgeHandler knowledge = Main.getHandler(player);
                if (!knowledge.hasKnowledge("strength")) {
                    if (!player.world.isRemote)
                        player.sendMessage(new TextComponentString(
                                "You discover this is a ring of strength!"));
                    knowledge.addKnowledge("strength");
                }
            }
        };
        new RingAction(group, "PARANOIA") {
            @Override
            public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
                if (Main.random.nextInt(200) > 198)
                    if (!player.world.isRemote) {
                        double d0 = player.posX;
                        double d1 = player.posY;
                        double d2 = player.posZ;
                        int i = Main.random.nextInt(SoundEvents.class
                                .getDeclaredFields().length);
                        try {
                            Object j = null;
                            j = SoundEvents.class.getDeclaredFields()[i].get(j);
                            player.world.playSound((EntityPlayer) null, d0, d1,
                                    d2, (SoundEvent) j, SoundCategory.PLAYERS,
                                    1.0F, 1.0F);
                            player.playSound((SoundEvent) j, 1.0F, 1.0F);
                        } catch (IllegalArgumentException e) {
                            player.playSound(SoundEvents.ENTITY_CREEPER_PRIMED,
                                    1.0F, 1.0F);
                        } catch (IllegalAccessException e) {
                            player.playSound(SoundEvents.ENTITY_ZOMBIE_AMBIENT,
                                    1.0F, 1.0F);
                        } catch (SecurityException e) {
                            player.playSound(SoundEvents.ENTITY_PLAYER_HURT,
                                    1.0F, 1.0F);
                        }

                    }
            }
        };
        if(Loader.isModLoaded("lilliputian")) {
            new RingAction(group, "GROWTH") {
                @Override
                public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
                    if (player.getActivePotionEffect(Potion.getPotionById(10)) == null)
                        player.addPotionEffect(getPotionGrowth());
                    IKnowledgeHandler knowledge = Main.getHandler(player);
                    if (!knowledge.hasKnowledge("growth")) {
                        if (!player.world.isRemote)
                            player.sendMessage(new TextComponentString(
                                    "You discover this is a ring of growth!"));
                        knowledge.addKnowledge("growth");
                    }
                }
            };
            new RingAction(group, "SHRINKING") {
                @Override
                public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
                    if (player.getActivePotionEffect(Potion.getPotionById(10)) == null)
                        player.addPotionEffect(getPotionShrink());
                    IKnowledgeHandler knowledge = Main.getHandler(player);
                    if (!knowledge.hasKnowledge("shrinking")) {
                        if (!player.world.isRemote)
                            player.sendMessage(new TextComponentString(
                                    "You discover this is a ring of shrinking!"));
                        knowledge.addKnowledge("shrinking");
                    }
                }
            };
        }
        }
    @Optional.Method(modid = "lilliputian")
    public static PotionEffect getPotionGrowth() {
        return new PotionEffect(lilliputian.potions.PotionLilliputian.GROWING.getEffects().get(0).getPotion(),10,0, false, false);
    }
    @Optional.Method(modid = "lilliputian")
    public static PotionEffect getPotionShrink() {
        return new PotionEffect(PotionLilliputian.SHRINKING.getEffects().get(0).getPotion(),10,0, false, false);
    }
    }

