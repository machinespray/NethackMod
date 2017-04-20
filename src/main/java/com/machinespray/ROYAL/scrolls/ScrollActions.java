package com.machinespray.ROYAL.scrolls;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.NetHackItem;
import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;

public class ScrollActions implements Constants {
	public static ArrayList<ScrollAction> actions = new ArrayList<ScrollAction>();
	private static Random random = new Random();
	private static ArrayList<Integer> ids = new ArrayList<Integer>();

	public static void initActions() {
		actions.clear();
		actions.add(new ScrollAction("identify") {
			@Override
			public void onItemRightClick(World worldIn, EntityPlayer playerIn,
					EnumHand handIn) {
				IKnowledgeHandler knowledge = Main.getHandler(playerIn);
				if (!knowledge.hasKnowledge("identify")) {
					if (!worldIn.isRemote)
						playerIn.sendMessage(new TextComponentString(
								"You discover this is a scroll of identify!"));
					knowledge.addKnowledge("identify");
				}
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
						// knowledge.addKnowledge(nhi.getUse(),nhi.getID());
					}
					NBTTagCompound nbt = playerIn.getHeldItemOffhand()
							.getTagCompound();
					nbt.setBoolean("BUCI", true);
					playerIn.getHeldItemOffhand().setTagCompound(nbt);
				}

			}
		});
		actions.add(new ScrollAction("create monster") {
			@Override
			public void onItemRightClick(World worldIn, EntityPlayer playerIn,
					EnumHand handIn) {
				IKnowledgeHandler knowledge = Main.getHandler(playerIn);
				if (!playerIn.world.isRemote) {
					if (!knowledge.hasKnowledge("create monster")) {
						if (!worldIn.isRemote)
							playerIn.sendMessage(new TextComponentString(
									"You discover this is a scroll of create monster!"));
						knowledge.addKnowledge("create monster");
					}
					World world = playerIn.world;
					if (!playerIn.isCreative())
						playerIn.inventory.decrStackSize(
								playerIn.inventory.currentItem, 1);
					String buc = playerIn.getHeldItemMainhand()
							.getTagCompound().getString("BUC");
					EntityLiving entity;
					if (buc.equals(BLESSED)) {
						for (int i = 0; i < Main.random.nextInt(21) + 5; i++) {
							entity = new EntitySilverfish(playerIn.world);
							entity.setPosition(
									playerIn.posX + Main.random.nextInt(5) - 2,
									playerIn.posY,
									playerIn.posZ + Main.random.nextInt(5) - 2);
							playerIn.world.spawnEntity(entity);
						}
					} else if (buc.equals(CURSED)) {
						for (int i = 0; i < Main.random.nextInt(4) + 1; i++) {
							entity = new EntityBlaze(playerIn.world);
							entity.setPosition(
									playerIn.posX + Main.random.nextInt(5) - 2,
									playerIn.posY,
									playerIn.posZ + Main.random.nextInt(5) - 2);
							playerIn.world.spawnEntity(entity);
						}
					}
					if (buc.equals(UNCURSED))
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
		});
	}

	public static void match(long seed) {
		ids.clear();
		random.setSeed(seed);
		for (int i = 0; i < actions.size(); i++) {
			ScrollAction I = actions.get(i);
			int id = random.nextInt(Constants.scrollNames.length);
			while (ids.contains(id)) {
				id = random.nextInt(Constants.scrollNames.length);
			}
			I.id = id;
			ids.add(id);
		}
	}

	public static ScrollAction getAction(String name) {
		int id = 100;
		name = name.split("\\.")[1].replace("_", " ");
		for (int i = 0; i < scrollNames.length; i++) {
			if (scrollNames[i].equals(name))
				id = i;
		}
		for (int i = 0; i < actions.size(); i++) {
			ScrollAction I = actions.get(i);
			if (id == I.id) {
				return I;
			}
		}
		return null;
	}
}
