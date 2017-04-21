package com.machinespray.ROYAL.scrolls;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.NetHackItem;
import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import com.machinespray.ROYAL.rings.RingAction;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
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
		actions.add(new ScrollAction("enchant weapon") {
			@Override
			public void onItemRightClick(World worldIn, EntityPlayer playerIn,
					EnumHand handIn) {
				IKnowledgeHandler knowledge = Main.getHandler(playerIn);
				if (!playerIn.world.isRemote) {
					World world = playerIn.world;
					if (!playerIn.isCreative())
						playerIn.inventory.decrStackSize(
								playerIn.inventory.currentItem, 1);
					String buc = playerIn.getHeldItemMainhand()
							.getTagCompound().getString("BUC");
					if (playerIn.getHeldItemOffhand() != null) {
						if (playerIn.getHeldItemOffhand().isItemEnchantable()) {
							Field[] enchantments = Enchantments.class
									.getDeclaredFields();
							Enchantment enchantment = null;
							if (buc.equals(UNCURSED) || buc.equals(BLESSED))
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
													buc.equals(BLESSED) ? enchantment
															.getMaxLevel()
															: enchantment
																	.getMinLevel()
																	+ Main.random
																			.nextInt(enchantment
																					.getMaxLevel()
																					+ 1
																					- enchantment
																							.getMinLevel()));
									if (!knowledge
											.hasKnowledge("enchant weapon")) {
										if (!worldIn.isRemote)
											playerIn.sendMessage(new TextComponentString(
													"You discover this is a scroll of enchant weapon!"));
										knowledge
												.addKnowledge("enchant weapon");
									}
								} catch (IllegalArgumentException e) {
								} catch (IllegalAccessException e) {
								}
							if (buc.equals(CURSED))
								playerIn.getHeldItemOffhand().addEnchantment(
										Enchantments.VANISHING_CURSE, 1);
						}
					} else {
						playerIn.sendMessage(new TextComponentString(
								"Your offhand glows for a second..."));
					}
				}
			}
		});
		actions.add(new ScrollAction("teleport") {
			@Override
			public void onItemRightClick(World worldIn, EntityPlayer player,
					EnumHand handIn) {
				if (!player.world.isRemote) {
					double d0 = player.posX;
					double d1 = player.posY;
					double d2 = player.posZ;

					for (int i = 0; i < 16; i++) {
						double d3 = player.posX
								+ (player.getRNG().nextDouble() - 0.5D) * 16.0D;
						double d4 = MathHelper.clamp(player.posY
								+ (double) (player.getRNG().nextInt(16) - 8),
								0.0D,
								(double) (player.world.getActualHeight() - 1));
						double d5 = player.posZ
								+ (player.getRNG().nextDouble() - 0.5D) * 16.0D;

						if (player.isRiding()) {
							player.dismountRidingEntity();
						}

						if (player.attemptTeleport(d3, d4, d5)) {
							player.world.playSound((EntityPlayer) null, d0, d1,
									d2, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
									SoundCategory.PLAYERS, 1.0F, 1.0F);
							player.playSound(
									SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
									1.0F, 1.0F);
							IKnowledgeHandler knowledge = Main
									.getHandler(player);
							if (!knowledge.hasKnowledge("teleport")) {
								if (!player.world.isRemote)
									player.sendMessage(new TextComponentString(
											"You discover this is a scroll of teleport!"));
								knowledge.addKnowledge("teleport");
							}
						}
						break;
					}
				}
			}

		});
		/*
		 * actions.add(new ScrollAction("taming") {
		 * 
		 * @Override public void onItemRightClick(World worldIn, EntityPlayer
		 * player, EnumHand handIn) { ItemStack itemstack =
		 * player.getHeldItemMainhand(); String buc =
		 * itemstack.getTagCompound().getString("BUC"); AxisAlignedBB box = new
		 * AxisAlignedBB(player.posX - 5, player.posY - 5, player.posZ - 5,
		 * player.posX + 5, player.posY + 5, player.posZ + 5); if
		 * (buc.equals(CURSED)) box.expandXyz(.5); if (buc.equals(BLESSED))
		 * box.expandXyz(2); List<EntityTameable> list =
		 * player.world.getEntitiesWithinAABB( EntityTameable.class, box); for
		 * (EntityTameable e : list) { if (e.getOwner() == null) {
		 * e.setOwnerId(player.getUniqueID()); }
		 * 
		 * } } });
		 */
		actions.add(new ScrollAction("destroy weapon") {
			@Override
			public void onItemRightClick(World worldIn, EntityPlayer playerIn,
					EnumHand handIn) {
				IKnowledgeHandler knowledge = Main.getHandler(playerIn);
				if (!playerIn.world.isRemote) {
					World world = playerIn.world;
					if (!playerIn.isCreative())
						playerIn.inventory.decrStackSize(
								playerIn.inventory.currentItem, 1);
					String buc = playerIn.getHeldItemMainhand()
							.getTagCompound().getString("BUC");
					if (playerIn.getHeldItemOffhand() != null) {
						playerIn.getHeldItemOffhand().setCount(
								playerIn.getHeldItemOffhand().getCount() - 1);
						if (!knowledge.hasKnowledge("destroy weapon")) {
							if (!playerIn.world.isRemote)
								playerIn.sendMessage(new TextComponentString(
										"You discover this is a scroll of destroy weapon!"));
							knowledge.addKnowledge("destroy weapon");
						}
					} else {
						playerIn.sendMessage(new TextComponentString(
								"Your offhand glows for a second..."));
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
