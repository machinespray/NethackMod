package com.machinespray.ROYAL.items.scrolls;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.Values;
import com.machinespray.ROYAL.items.NetHackItem;
import com.machinespray.ROYAL.items.randomized.IRandomizedClass;
import com.machinespray.ROYAL.render.RenderGUIEvent;
import com.machinespray.ROYAL.sync.knowledge.IKnowledgeHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntitySilverfish;
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
import java.util.ArrayList;
import java.util.Random;

public enum ScrollAction implements Constants {
    IDENTIFY, CREATE_MONSTER, ENCHANT_WEAPON, TELEPORT, DESTROY_WEAPON;
    private static Random random = new Random();
    private static ArrayList<Integer> ids = new ArrayList<Integer>();
    public int id;

    public String getKnowledgeName() {
        return this.name().replace("_", " ").toLowerCase();
    }

    private void discover(Entity player) {
        IKnowledgeHandler knowledge = Main.getHandler(player);
        if (!knowledge.hasKnowledge(getKnowledgeName())) {
            if (!player.world.isRemote)
                RenderGUIEvent.buffer.add(
                        "You discover this is a scroll of\n" + getKnowledgeName() + "!");
            knowledge.addKnowledge(getKnowledgeName());
        }
    }

    public void onItemRightClick(World worldIn,
                                 EntityPlayer playerIn, EnumHand handIn) {
        IKnowledgeHandler knowledge = Main.getHandler(playerIn);
        if (this.equals(IDENTIFY)) {
            discover(playerIn);
            if (!playerIn.isCreative())
                playerIn.inventory.decrStackSize(
                        playerIn.inventory.currentItem, 1);
            if (playerIn.getHeldItemOffhand().getItem() instanceof NetHackItem && playerIn.getHeldItemOffhand().getItem() instanceof IRandomizedClass) {
                NetHackItem nhi = (NetHackItem) playerIn
                        .getHeldItemOffhand().getItem();
                IRandomizedClass rc = (IRandomizedClass) nhi;
                if (rc.hasUse())
                    if (!knowledge.hasKnowledge(rc.getUse())) {
                        if (!worldIn.isRemote)
                            RenderGUIEvent.buffer.add(
                                    "You discover the item\nin your offhand is a\n"
                                            + rc.type() + " of "
                                            + rc.getUse() + "!");
                        knowledge.addKnowledge(rc.getUse());
                    }
                NBTTagCompound nbt = playerIn.getHeldItemOffhand()
                        .getTagCompound() != null ? playerIn.getHeldItemOffhand().getTagCompound() : new NBTTagCompound();
                nbt.setBoolean(BUCI, true);
                playerIn.getHeldItemOffhand().setTagCompound(nbt);
            }
        } else if (this.equals(CREATE_MONSTER)) {
            if (!playerIn.world.isRemote) {
                discover(playerIn);
                World world = playerIn.world;
                if (!playerIn.isCreative())
                    playerIn.inventory.decrStackSize(
                            playerIn.inventory.currentItem, 1);
                String buc = playerIn.getHeldItemMainhand()
                        .getTagCompound().getString(BUC);
                EntityLiving entity;
                if (buc.equals(BLESSED)) {
                    for (int i = 0; i < Values.random.nextInt(21) + 5; i++) {
                        entity = new EntitySilverfish(playerIn.world);
                        entity.setPosition(
                                playerIn.posX + Values.random.nextInt(5) - 2,
                                playerIn.posY,
                                playerIn.posZ + Values.random.nextInt(5) - 2);
                        playerIn.world.spawnEntity(entity);
                    }
                } else if (buc.equals(CURSED)) {
                    for (int i = 0; i < Values.random.nextInt(4) + 1; i++) {
                        entity = new EntityBlaze(playerIn.world);
                        entity.setPosition(
                                playerIn.posX + Values.random.nextInt(5) - 2,
                                playerIn.posY,
                                playerIn.posZ + Values.random.nextInt(5) - 2);
                        playerIn.world.spawnEntity(entity);
                    }
                }
                if (buc.equals(UNCURSED))
                    for (int i = 0; i < Values.random.nextInt(4) + 1; i++) {
                        entity = new EntityZombie(playerIn.world);
                        entity.setPosition(
                                playerIn.posX + Values.random.nextInt(5) - 2,
                                playerIn.posY,
                                playerIn.posZ + Values.random.nextInt(5) - 2);
                        playerIn.world.spawnEntity(entity);
                    }

            }

        } else if (this.equals(ENCHANT_WEAPON)) {
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
                        if (buc.equals(UNCURSED) || buc.equals(BLESSED))
                            try {
                                enchantment = (Enchantment) enchantments[Values.random
                                        .nextInt(enchantments.length)]
                                        .get(enchantment);
                                while (!enchantment.canApply(playerIn
                                        .getHeldItemOffhand()))
                                    enchantment = (Enchantment) enchantments[Values.random
                                            .nextInt(enchantments.length)]
                                            .get(enchantment);
                                playerIn.getHeldItemOffhand()
                                        .addEnchantment(
                                                enchantment,
                                                buc.equals(BLESSED) ? enchantment
                                                        .getMaxLevel()
                                                        : enchantment
                                                        .getMinLevel()
                                                        + Values.random
                                                        .nextInt(enchantment
                                                                .getMaxLevel()
                                                                + 1
                                                                - enchantment
                                                                .getMinLevel()));
                                discover(playerIn);
                            } catch (IllegalArgumentException e) {
                            } catch (IllegalAccessException e) {
                            }
                        if (buc.equals(CURSED))
                            playerIn.getHeldItemOffhand().addEnchantment(
                                    Enchantments.VANISHING_CURSE, 1);
                    } else
                        playerIn.sendMessage(new TextComponentString(
                                "Your offhand glows for a second..."));
                } else {
                    playerIn.sendMessage(new TextComponentString(
                            "Your offhand glows for a second..."));
                }
            }
        } else if (this.equals(TELEPORT)) {
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
        } else if (this.equals(DESTROY_WEAPON)) {
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
    }

}
