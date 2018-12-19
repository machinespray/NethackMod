package com.machinespray.ROYAL.action.scrolls;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.NetHackItem;
import com.machinespray.ROYAL.action.Discovery;
import com.machinespray.ROYAL.sync.MessageSendKnowledge;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Random;

public enum ScrollAction implements Constants, Discovery {
	IDENTIFY, CREATE_MONSTER, ENCHANT_WEAPON, RECALL, SMITE;
	public static ArrayList<Integer> ids = new ArrayList<>();
	private static Random random = new Random();
	public int id;

	public static void match(long seed) {
		ids.clear();
		random.setSeed(seed);
		for (int i = 0; i < values().length; i++) {
			ScrollAction I = values()[i];
			int id = random.nextInt(Constants.scrollNames.length);
			while (ids.contains(id)) {
				id = random.nextInt(Constants.scrollNames.length);
			}
			I.id = id;
			ids.add(id);
		}
	}

	public static ScrollAction getAction(String name) {
		int id = -1;
		name = name.split("\\.")[1].replace("_", " ");
		for (int i = 0; i < scrollNames.length; i++) {
			if (scrollNames[i].equals(name)) {
				id = i;
				break;
			}
		}
		for (int i = 0; i < values().length; i++) {
			ScrollAction I = values()[i];
			if (id == I.id)
				return I;
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public static void match(MessageSendKnowledge message) {
		ids.ensureCapacity(message.knowledge);
		ids.set(message.knowledge, message.id);
		values()[message.knowledge].id = message.id;
	}

	private void discover(EntityPlayer player, boolean sound) {
		if (player instanceof EntityPlayerMP)
			discover((EntityPlayerMP) player, getKnowledgeName(), sound);
	}

	public String getKnowledgeName() {
		return this.name().replace("_", " ").toLowerCase();
	}

	public void onItemRightClick(EntityPlayer playerIn) {
		//Remove scroll if not in creative
		if (!playerIn.isCreative())
			playerIn.inventory.decrStackSize(playerIn.inventory.currentItem, 1);
		switch (this) {
			case IDENTIFY:
				if (playerIn.getHeldItemOffhand().getItem() instanceof NetHackItem) {
					NetHackItem item = (NetHackItem) playerIn.getHeldItemOffhand().getItem();
					if (item.hasUse())
						if (!Main.getHandler(playerIn).hasKnowledge(item.getUse())) {
							boolean sound = Main.getHandler(playerIn).hasKnowledge("identify");
							discover(playerIn, !sound);
							discover((EntityPlayerMP) playerIn, item.getUse(), sound);
							return;
						}
				}
				playerIn.world.playSound(null, playerIn.getPosition(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.BLOCKS, 0.5F, 1.0F);
				return;
			case CREATE_MONSTER:
				discover(playerIn, true);
				EntityLiving entity;
				for (int i = 0; i < Main.random.nextInt(4) + 3; i++) {
					entity = new EntityZombie(playerIn.world);
					entity.setPosition(
							playerIn.posX + Main.random.nextInt(5) - 2,
							playerIn.posY,
							playerIn.posZ + Main.random.nextInt(5) - 2);
					playerIn.world.spawnEntity(entity);
				}
				return;
			case ENCHANT_WEAPON:
				ItemStack enchantable = playerIn.getHeldItemOffhand();
				if (enchantable.isItemEnchantable()) {
					discover(playerIn, true);
					EnchantmentHelper.addRandomEnchantment(Main.random, enchantable, 10, true);
					return;
				}
				playerIn.world.playSound(null, playerIn.getPosition(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.BLOCKS, 0.5F, 1.0F);
				return;
			case RECALL:
				if (playerIn.dimension != 0)
					return;
				BlockPos pos = playerIn.getBedLocation();
				if (pos == null)
					pos = playerIn.world.provider.getRandomizedSpawnPoint();
				discover(playerIn, true);
				playerIn.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
				return;
			case SMITE:
				discover(playerIn, true);
				World world = playerIn.world;
				BlockPos blockPos = new BlockPos(playerIn.posX, playerIn.posY, playerIn.posZ);
				blockPos = blockPos.add(
						Main.random.nextInt(11) - 5,
						10,
						Main.random.nextInt(11) - 5);
				int i = 0;
				while (!world.getBlockState(blockPos).isSideSolid(world, blockPos, EnumFacing.UP) && i < 21) {
					blockPos = blockPos.down();
					i++;
				}
				EntityLightningBolt bolt = new EntityLightningBolt(
						playerIn.world,
						blockPos.getX(),
						blockPos.getY(),
						blockPos.getZ(),
						false
				);
				playerIn.world.addWeatherEffect(bolt);
		}
	}

}
