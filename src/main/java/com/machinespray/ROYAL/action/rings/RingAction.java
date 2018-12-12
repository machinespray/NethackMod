package com.machinespray.ROYAL.action.rings;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.action.Discovery;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.errors.UndefinedPotionError;
import com.machinespray.ROYAL.sync.MessageSendKnowledge;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum RingAction implements Constants, Discovery {
	AGGRAVATE_MONSTER, CONFLICT, LEVITATION, VISION, TELEPORTATION, NOTHING, PROTECTION, REGENERATION, FIRE_RESISTANCE, STRENGTH, PARANOIA;

	public int id;

	private static RingAction[] potionRings = {PROTECTION, REGENERATION, FIRE_RESISTANCE, STRENGTH};
	private static int[] potionIds = {11, 10, 12, 5};

	public static ArrayList<Integer> ids = new ArrayList<>();
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
			if (ringNames[i].equals(name)) {
				id = i;
				break;
			}
		}
		for (int i = 0; i < RingAction.values().length; i++) {
			RingAction I = RingAction.values()[i];
			if (id == I.id)
				return I;
		}
		return null;
	}

	public String getKnowledgeName() {
		return this.name().replace("_", " ").toLowerCase();
	}

	public void onWornTick(EntityLivingBase living) {
		if (Arrays.asList(potionRings).contains(this)) {
			for (int i = 0; i < potionRings.length; i++)
				if (potionRings[i].equals(this)) {
					addPotionEffect(potionIds[i], living);
					break;
				}
			return;
		}
		List<EntityLiving> list = getLocalLiving(living);
		switch (this) {
			case AGGRAVATE_MONSTER:
				if (living instanceof EntityPlayer)
					if (((EntityPlayer) living).isCreative())
						return;
				for (EntityLiving e : list) {
					if (e.getAttackTarget() == null || e.getAttackTarget() != living) {
						e.setAttackTarget(living);
					}
				}
				return;
			case CONFLICT:
				for (EntityLiving e : list) {
					if (e.getAttackTarget() == null) {
						EntityLiving entity = list.get(Main.random.nextInt(list.size()));
						if (!entity.equals(e))
							e.setAttackTarget(entity);
					}
				}
				return;
			case LEVITATION:
				if (!(living instanceof EntityPlayer))
					return;
				doLevitation((EntityPlayer) living);
				discover((EntityPlayer) living);
				return;
			case PARANOIA:
				if (living instanceof EntityPlayer)
					if (Main.random.nextInt(1000) > 998)
						if (!living.world.isRemote) {
							double d0 = living.posX;
							double d1 = living.posY;
							double d2 = living.posZ;
							int i = Main.random.nextInt(SoundEvents.class.getDeclaredFields().length);
							try {
								Object j;
								j = SoundEvents.class.getDeclaredFields()[i].get(null);
								living.world.playSound((EntityPlayer) living, d0, d1,
										d2, (SoundEvent) j, SoundCategory.PLAYERS,
										1.0F, 1.0F);
								living.playSound((SoundEvent) j, 1.0F, 1.0F);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
		}

	}


	private void discover(EntityPlayer player) {
		if (player instanceof EntityPlayerMP)
			discover((EntityPlayerMP) player, getKnowledgeName(), true);
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

	@SideOnly(Side.CLIENT)
	public static void match(MessageSendKnowledge message) {
		ids.ensureCapacity(message.knowledge);
		ids.set(message.knowledge, message.id);
		values()[message.knowledge].id = message.id;
	}

	public void clientAction(EntityLivingBase player) {
		EntityPlayer entityPlayer = (EntityPlayer) player;
		if (this.equals(LEVITATION))
			doLevitation(entityPlayer);
	}

	private void doLevitation(EntityPlayer entityPlayer) {
		if (entityPlayer.isInWater()) {
			entityPlayer.onGround = true;
			entityPlayer.motionY = 0;
			entityPlayer.stepHeight = 0.1F;
		}
		entityPlayer.fallDistance = 0.0F;
		entityPlayer.stepHeight = 2.0F;
		if (entityPlayer.onGround) {
			entityPlayer.posY += 0.35;
		} else if (entityPlayer.motionY < 0.0D) {
			entityPlayer.motionY *= 0.6D;
		}
		if (entityPlayer.onGround) {
			entityPlayer.motionX = levitationMovement(entityPlayer.motionX);
			entityPlayer.motionZ = levitationMovement(entityPlayer.motionZ);
		}
	}

	private double levitationMovement(double speed) {
		speed *= 1.2;
		speed = Math.min(speed, 0.5);
		speed = Math.max(speed, -0.5);
		return speed;
	}
}
