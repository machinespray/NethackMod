package com.machinespray.ROYAL.rings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;

public class RingActions implements Constants {
	static ArrayList<RingAction> actions = new ArrayList<RingAction>();
	private static Random random = new Random();
	private static ArrayList<Integer> ids = new ArrayList<Integer>();
	private static SoundEvent[] sounds = {
			SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
			SoundEvents.ENTITY_BLAZE_DEATH, SoundEvents.ENTITY_CREEPER_PRIMED };

	public static void initActions() {
		actions.add(new RingAction("aggrivate monster") {
			@Override
			public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
				List<EntityLiving> list = player.world.getEntitiesWithinAABB(
						EntityLiving.class, new AxisAlignedBB(player.posX - 5,
								player.posY - 5, player.posZ - 5,
								player.posX + 5, player.posY + 5,
								player.posZ + 5));
				for (EntityLiving e : list) {
					if (e.getAttackTarget() == null
							|| e.getAttackTarget() != player) {
						e.setAttackTarget(player);
					}
				}
			}
		});
		actions.add(new RingAction("conflict") {
			@Override
			public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
				List<EntityLiving> list = player.world.getEntitiesWithinAABB(
						EntityLiving.class, new AxisAlignedBB(player.posX - 5,
								player.posY - 5, player.posZ - 5,
								player.posX + 5, player.posY + 5,
								player.posZ + 5));
				for (EntityLiving e : list) {
					if (e.getAttackTarget() == null) {
						e.setAttackTarget(list.get(Main.random.nextInt(list
								.size())));
					}
					if (e.getAttackTarget() instanceof EntityPlayer) {
						e.setAttackTarget(list.get(Main.random.nextInt(list
								.size())));
					}
					if (e.getAttackTarget().equals(e)) {
						e.setAttackTarget(list.get(Main.random.nextInt(list
								.size())));
					}
				}

			}
		});
		actions.add(new RingAction("levitation") {
			@Override
			public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
				player.addPotionEffect(new PotionEffect(Potion
						.getPotionById(25), 20, -1, false, false));
				player.addPotionEffect(new PotionEffect(
						Potion.getPotionById(1), 20, 255, false, false));
				if (player.world.getBlockState(new BlockPos(player.posX,
						player.posY - 2, player.posZ)) == Blocks.AIR
						.getStateFromMeta(0)
						&& player.world.getBlockState(new BlockPos(player.posX,
								player.posY - 2, player.posZ)) == Blocks.AIR
								.getStateFromMeta(0)) {
					player.setPosition(player.posX, player.posY - 0.1,
							player.posZ);
				}
				if (player.world.getBlockState(new BlockPos(player.posX,
						player.posY - 1, player.posZ)) != Blocks.AIR
						.getStateFromMeta(0)
						&& !(player.world.getBlockState(new BlockPos(
								player.posX, player.posY - 1, player.posZ))
								.isTranslucent())
						&& (player.world.getBlockState(new BlockPos(
								player.posX, player.posY - 1, player.posZ))
								.causesSuffocation())) {
					player.setPosition(player.posX, player.posY + .2,
							player.posZ);
				}
			}
		});
		actions.add(new RingAction("see invisible") {
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
		});
		actions.add(new RingAction("teleportation") {
			@Override
			public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
				if (Main.random.nextInt(200) > 198)
					if (!player.world.isRemote) {
						double d0 = player.posX;
						double d1 = player.posY;
						double d2 = player.posZ;

						for (int i = 0; i < 16; ++i) {
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
								break;
							}
						}
					}

			}
		});
		actions.add(new RingAction("hunger") {
			@Override
			public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
				player.addPotionEffect(new PotionEffect(Potion
						.getPotionById(17), 21, 0, false, false));
			}
		});
		actions.add(new RingAction("protection") {
			@Override
			public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
				player.addPotionEffect(new PotionEffect(Potion
						.getPotionById(11), 21, 0, false, false));
			}
		});
		actions.add(new RingAction("regenaration") {
			@Override
			public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
				player.addPotionEffect(new PotionEffect(Potion
						.getPotionById(11), 10, 0, false, false));
			}
		});
		actions.add(new RingAction("fire resistance") {
			@Override
			public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
				player.addPotionEffect(new PotionEffect(Potion
						.getPotionById(12), 10, 0, false, false));
			}
		});
		actions.add(new RingAction("strength") {
			@Override
			public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
				player.addPotionEffect(new PotionEffect(
						Potion.getPotionById(5), 10, 0, false, false));
			}
		});
		actions.add(new RingAction("paranoia") {
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
							player.world.playSound((EntityPlayer) null, d0,
									d1, d2,
									(SoundEvent) j,
									SoundCategory.PLAYERS, 1.0F, 1.0F);
							player.playSound((SoundEvent) j, 1.0F, 1.0F);
						} catch (IllegalArgumentException e) {
							player.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 1.0F);
						} catch (IllegalAccessException e) {
							player.playSound(SoundEvents.ENTITY_ZOMBIE_AMBIENT, 1.0F, 1.0F);
						} catch (SecurityException e) {
							player.playSound(SoundEvents.ENTITY_PLAYER_HURT, 1.0F, 1.0F);
						}

					}

			}
		});

	}

	public static void match(long seed) {
		ids.clear();
		random.setSeed(seed);
		for (int i = 0; i < actions.size(); i++) {
			RingAction I = actions.get(i);
			int id = random.nextInt(Constants.ringNames.length);
			while (ids.contains(id)) {
				id = random.nextInt(Constants.ringNames.length);
			}
			I.id = id;
			ids.add(id);
		}
	}

	public static RingAction getAction(String name) {
		int id = 100;
		name = name.split("R|\\.")[1].replace("_", "");
		for (int i = 0; i < ringNames.length; i++) {
			if (ringNames[i].equals(name))
				id = i;
		}
		for (int i = 0; i < actions.size(); i++) {
			RingAction I = actions.get(i);
			if (id == I.id)
				return I;
		}
		return null;
	}
}
