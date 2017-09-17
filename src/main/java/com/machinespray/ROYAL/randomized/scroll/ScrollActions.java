package com.machinespray.ROYAL.randomized.scroll;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.NetHackItem;
import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.lang.reflect.Field;

public class ScrollActions implements Constants {
    public static void init(ScrollActionGroup group) {
        new ScrollAction(group, "IDENTIFY") {
            @Override
            public void onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
                IKnowledgeHandler knowledge = Main.getHandler(playerIn);
                if (!playerIn.isCreative())
                    playerIn.inventory.decrStackSize(
                            playerIn.inventory.currentItem, 1);
                if (playerIn.getHeldItemOffhand().getItem() instanceof NetHackItem) {
                    NetHackItem nhi = (NetHackItem) playerIn
                            .getHeldItemOffhand().getItem();
                    if (!knowledge.hasKnowledge(nhi.getUse())) {
                        if (!worldIn.isRemote)
                            playerIn.sendMessage(new TextComponentString(
                                    "You discover the item in your offhand is a "
                                            + nhi.type() + " of "
                                            + nhi.getUse() + "!"));
                        knowledge.addKnowledge(nhi.getUse());
                    }
                    NBTTagCompound nbt = playerIn.getHeldItemOffhand()
                            .getTagCompound();
                    nbt.setBoolean(BUCI, true);
                    playerIn.getHeldItemOffhand().setTagCompound(nbt);
                }
            }
        };
        new ScrollAction(group, "CREATE_MONSTER") {
            @Override
            public void onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
                if (!playerIn.world.isRemote) {
                    discover(playerIn);
                    World world = playerIn.world;
                    if (!playerIn.isCreative())
                        playerIn.inventory.decrStackSize(
                                playerIn.inventory.currentItem, 1);
                    String buc = playerIn.getHeldItemMainhand()
                            .getTagCompound().getString(BUC);
                    EntityLiving entity;
                    for (int i = 0; i < Main.random.nextInt(4) + 1; i++) {
                        entity = new EntityZombie(playerIn.world);
                        entity.setPosition(
                                playerIn.posX + Main.random.nextInt(5) - 2,
                                playerIn.posY,
                                playerIn.posZ + Main.random.nextInt(5) - 2);
                        playerIn.world.spawnEntity(entity);
                    }
                }
            }
        };
        new ScrollAction(group, "ENCHANT_WEAPON") {
            @Override
            public void onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
                if (!playerIn.world.isRemote) {
                    World world = playerIn.world;
                    if (!playerIn.isCreative())
                        playerIn.inventory.decrStackSize(
                                playerIn.inventory.currentItem, 1);
                    String buc = playerIn.getHeldItemMainhand()
                            .getTagCompound().getString(BUC);
                    if (playerIn.getHeldItemOffhand() != null) {
                        if (playerIn.getHeldItemOffhand().isItemEnchantable()) {
                            Field[] enchantments = Enchantments.class
                                    .getDeclaredFields();
                            Enchantment enchantment = null;
                            try {
                                enchantment = (Enchantment) enchantments[Main.random
                                        .nextInt(enchantments.length)]
                                        .get(enchantment);
                                while (!enchantment.canApply(playerIn
                                        .getHeldItemOffhand()))
                                    enchantment = (Enchantment) enchantments[Main.random
                                            .nextInt(enchantments.length)]
                                            .get(enchantment);
                                playerIn.getHeldItemOffhand()
                                        .addEnchantment(
                                                enchantment,
                                                enchantment
                                                        .getMinLevel()
                                                        + Main.random
                                                        .nextInt(enchantment
                                                                .getMaxLevel()
                                                                + 1
                                                                - enchantment
                                                                .getMinLevel()));
                                discover(playerIn);
                            } catch (IllegalArgumentException e) {
                            } catch (IllegalAccessException e) {
                            }
                            //if (buc.equals(CURSED))
                            //playerIn.getHeldItemOffhand().addEnchantment(
                            //Enchantments.VANISHING_CURSE, 1);
                        } else
                            playerIn.sendMessage(new TextComponentString(
                                    "Your offhand glows for a second..."));
                    } else {
                        playerIn.sendMessage(new TextComponentString(
                                "Your offhand glows for a second..."));
                    }
                }
            }
        };
        new ScrollAction(group, "TELEPORT") {
            @Override
            public void onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
                if (!playerIn.world.isRemote) {
                    double d0 = playerIn.posX;
                    double d1 = playerIn.posY;
                    double d2 = playerIn.posZ;

                    for (int i = 0; i < 16; i++) {
                        double d3 = playerIn.posX
                                + (playerIn.getRNG().nextDouble() - 0.5D) * 16.0D;
                        double d4 = MathHelper.clamp(playerIn.posY
                                        + (double) (playerIn.getRNG().nextInt(16) - 8),
                                0.0D,
                                (double) (playerIn.world.getActualHeight() - 1));
                        double d5 = playerIn.posZ
                                + (playerIn.getRNG().nextDouble() - 0.5D) * 16.0D;

                        if (playerIn.isRiding()) {
                            playerIn.dismountRidingEntity();
                        }

                        if (playerIn.attemptTeleport(d3, d4, d5)) {
                            playerIn.world.playSound((EntityPlayer) null, d0, d1,
                                    d2, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
                                    SoundCategory.PLAYERS, 1.0F, 1.0F);
                            playerIn.playSound(
                                    SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
                                    1.0F, 1.0F);
                            discover(playerIn);
                            if (!playerIn.isCreative())
                                playerIn.inventory.decrStackSize(
                                        playerIn.inventory.currentItem, 1);
                        }
                        break;
                    }
                }
            }
        };
        new ScrollAction(group, "DESTROY_WEAPON") {

            @Override
            public void onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
                if (!playerIn.world.isRemote) {
                    World world = playerIn.world;
                    if (!playerIn.isCreative())
                        playerIn.inventory.decrStackSize(
                                playerIn.inventory.currentItem, 1);
                    String buc = playerIn.getHeldItemMainhand()
                            .getTagCompound().getString(BUC);
                    if (playerIn.getHeldItemOffhand() != null)
                        if (playerIn.getHeldItemOffhand().isItemEnchantable()) {
                            playerIn.getHeldItemOffhand().setCount(
                                    playerIn.getHeldItemOffhand().getCount() - 1);
                            discover(playerIn);
                        }
                } else {
                    playerIn.sendMessage(new TextComponentString(
                            "Your offhand glows for a second..."));
                }
            }
        };
    }
}
