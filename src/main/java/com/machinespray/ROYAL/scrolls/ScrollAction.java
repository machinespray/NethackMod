package com.machinespray.ROYAL.scrolls;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.NetHackItem;
import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import com.machinespray.ROYAL.sync.MessageSendKnowledge;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public enum ScrollAction implements Constants {
	IDENTIFY, CREATE_MONSTER, ENCHANT_WEAPON, TELEPORT, DESTROY_WEAPON;
	public int id;

	public String getKnowledgeName() {
		return this.name().replace("_", " ").toLowerCase();
	}

	private void discover(EntityPlayer player){
		discover(player,this.getKnowledgeName());
	}
	private void discover(Entity player,String knowledgeName) {
		IKnowledgeHandler knowledge = Main.getHandler(player);
		if (!knowledge.hasKnowledge(knowledgeName)) {
			player.sendMessage(new TextComponentString(
					"You discover this is a scroll of " + knowledgeName + "!"));
			knowledge.addKnowledge(knowledgeName);
			Main.INSTANCE.sendTo(new MessageSendKnowledge(knowledgeName), (EntityPlayerMP) player);
		}
	}

	public void onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		//Remove scroll if not in creative
		if (!playerIn.isCreative())
			playerIn.inventory.decrStackSize(playerIn.inventory.currentItem, 1);

		if (this.equals(IDENTIFY)) {

			discover(playerIn);

			if (playerIn.getHeldItemOffhand().getItem() instanceof NetHackItem) {
				NetHackItem item = (NetHackItem)playerIn.getHeldItemOffhand().getItem();
				if(item.hasUse())
					discover(playerIn,item.getUse());
			}
		} else if (this.equals(CREATE_MONSTER)) {
			if (!playerIn.world.isRemote) {
				discover(playerIn);
				EntityLiving entity;
				for (int i = 0; i < Main.random.nextInt(4) + 3; i++) {
					entity = new EntityZombie(playerIn.world);
					entity.setPosition(
							playerIn.posX + Main.random.nextInt(5) - 2,
							playerIn.posY,
							playerIn.posZ + Main.random.nextInt(5) - 2);
					playerIn.world.spawnEntity(entity);
				}
			}
		} else if (this.equals(ENCHANT_WEAPON)) {
			ItemStack enchantable = playerIn.getHeldItemOffhand();

			if (enchantable.isItemEnchantable()) {
				EnchantmentHelper.addRandomEnchantment(Main.random, enchantable, 10, true);
/*
				Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(enchantable);
				ArrayList<Enchantment> appliable = new ArrayList<>();
				for (Enchantment enchantment : Enchantment.REGISTRY)
					if (enchantment.canApply(enchantable))
						appliable.add(enchantment);
				for(Enchantment e : appliable){
				}*/
			}
		} else if (this.equals(TELEPORT)) {

		} else if (this.equals(DESTROY_WEAPON)) {
			if (playerIn.getHeldItemOffhand().isItemEnchantable()) {
				playerIn.getHeldItemOffhand().setCount(
						playerIn.getHeldItemOffhand().getCount() - 1);
				discover(playerIn);
			} else {
				playerIn.sendMessage(new TextComponentString(
						"Your offhand glows for a second..."));
			}
		}

	}


	private static Random random = new Random();
	public static ArrayList<Integer> ids = new ArrayList<Integer>();

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

}
