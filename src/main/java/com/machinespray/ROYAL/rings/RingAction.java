package com.machinespray.ROYAL.rings;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.errors.UndefinedPotionError;
import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import com.machinespray.ROYAL.sync.MessageSendKnowledge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public enum RingAction implements Constants {
	AGGRAVATE_MONSTER, CONFLICT, LEVITATION, SEE_INVISIBLE, TELEPORTATION, NOTHING, PROTECTION, REGENERATION, FIRE_RESISTANCE, STRENGTH, PARANOIA;

	public int id;

	private static RingAction[] potionRings = {PROTECTION, REGENERATION, FIRE_RESISTANCE, STRENGTH};
	private static int[] potionIds = {11, 10, 12, 5};

	private static ArrayList<Integer> ids = new ArrayList<>();
	private static Random random = new Random();

	public static void match(long seed) {
		ids.clear();
		random.setSeed(seed);
		for (int i = 0; i < RingAction.values().length; i++) {
			RingAction I = RingAction.values()[i];
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
		name = name.split("\\.")[1].replace("_", " ");
		name = name.substring(0, name.length() - 1);
		for (int i = 0; i < ringNames.length; i++) {
			if (ringNames[i].equals(name))
				id = i;
		}
		for (int i = 0; i < RingAction.values().length; i++) {
			RingAction I = RingAction.values()[i];
			if (id == I.id) {
				return I;
			}
		}
		return null;
	}

	public String getKnowledgeName() {
		return this.name().replace("_", " ").toLowerCase();
	}

	public void onWornTick(EntityLivingBase player) {
		if (Arrays.asList(potionRings).contains(this)) {
			for (int i = 0; i < potionRings.length; i++)
				if (potionRings[i].equals(this)) {
					addPotionEffect(potionIds[i], player);
					break;
				}
		} else if (this.equals(AGGRAVATE_MONSTER)) {
			if (player instanceof EntityPlayer)
				if (((EntityPlayer) player).isCreative())
					return;
			for (EntityLiving e : getLocalLiving(player)) {
				if (e.getAttackTarget() == null
						|| e.getAttackTarget() != player) {
					e.setAttackTarget(player);
				}
			}
		} else if (this.equals(CONFLICT)) {
			List<EntityLiving> list = getLocalLiving(player);
			for (EntityLiving e : list) {
				if (e.getAttackTarget() == null) {
					EntityLiving entity = list.get(Main.random.nextInt(list
							.size()));
					if (!entity.equals(e))
						e.setAttackTarget(entity);
				}

			}
		} else if (this.equals(LEVITATION)) {
			player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:levitation")
					, 20, -1, false, false));
			player.addPotionEffect(new PotionEffect(
					Potion.getPotionFromResourceLocation("minecraft:speed"), 20, 255, false, false));
			discover(player);

		} else if (this.equals(SEE_INVISIBLE)) {
			List<EntityLiving> list = player.world.getEntitiesWithinAABB(
					EntityLiving.class, new AxisAlignedBB(player.posX - 5,
							player.posY - 5, player.posZ - 5,
							player.posX + 5, player.posY + 5,
							player.posZ + 5));
			for (EntityLiving e : list) {
				addPotionEffect(24, e);
			}
		} else if (this.equals(TELEPORTATION)) {
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
							player.world.playSound(null, d0,
									d1, d2,
									SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
									SoundCategory.PLAYERS, 1.0F, 1.0F);
							player.playSound(
									SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
									1.0F, 1.0F);
							discover(player);
							break;
						}

					}
				}
		} else if (this.equals(PARANOIA)) {
			if (player instanceof EntityPlayer)
				if (Main.random.nextInt(1000) > 998)
					if (!player.world.isRemote) {
						double d0 = player.posX;
						double d1 = player.posY;
						double d2 = player.posZ;
						int i = Main.random.nextInt(SoundEvents.class
								.getDeclaredFields().length);
						try {
							Object j;
							j = SoundEvents.class.getDeclaredFields()[i].get(null);
							player.world.playSound((EntityPlayer) player, d0, d1,
									d2, (SoundEvent) j, SoundCategory.PLAYERS,
									1.0F, 1.0F);
							player.playSound((SoundEvent) j, 1.0F, 1.0F);
						}catch (IllegalAccessException e){
							e.printStackTrace();
						}
					}
		}
	}

	private void discover(Entity player) {
		if (player instanceof EntityPlayerMP) {
			IKnowledgeHandler knowledge = Main.getHandler(player);
			if (!knowledge.hasKnowledge(getKnowledgeName())) {
				player.sendMessage(new TextComponentString(
						"You discover this is a ring of " + getKnowledgeName() + "!"));
				knowledge.addKnowledge(getKnowledgeName());
				Main.INSTANCE.sendTo(new MessageSendKnowledge(getKnowledgeName()), (EntityPlayerMP) player);
			}
		} else {
			Logger.getGlobal().warning("ROYAL:Discovery packet attempted to be sent from client-side");
		}
	}

	private List<EntityLiving> getLocalLiving(EntityLivingBase player) {
		AxisAlignedBB box = new AxisAlignedBB(player.posX - 5,
				player.posY - 5, player.posZ - 5, player.posX + 5,
				player.posY + 5, player.posZ + 5);
		return player.world.getEntitiesWithinAABB(
				EntityLiving.class, box);
	}

	private void addPotionEffect(int id, EntityLivingBase entity) throws UndefinedPotionError {
		Potion effect = Potion.getPotionById(id);
		if (effect == null)
			throw new UndefinedPotionError(Integer.toString(id));
		if (entity.getActivePotionEffect(effect) == null)
			entity.addPotionEffect(new PotionEffect(effect, 80, 0, false, false));
	}

}