package com.machinespray.ROYAL.scrolls;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.NetHackItem;
import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;

public class ScrollActions implements Constants {
	static ArrayList<ScrollAction> actions = new ArrayList<ScrollAction>();
	private static Random random = new Random();
	private static ArrayList<Integer> ids = new ArrayList<Integer>();

	public static void initActions() {
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
					if(!playerIn.isCreative())
					playerIn.inventory.decrStackSize(playerIn.inventory.currentItem, 1);
					if(playerIn.getHeldItemOffhand().getItem() instanceof NetHackItem){
						NetHackItem nhi = (NetHackItem) playerIn.getHeldItemOffhand().getItem();
						if(!knowledge.hasKnowledge(nhi.getUse())){
							if (!worldIn.isRemote)
							playerIn.sendMessage(new TextComponentString(
									"You discover the item in your offhand is a "+nhi.type()+" of "+nhi.getUse()+"!"));
							knowledge.addKnowledge(nhi.getUse());
						}
						NBTTagCompound nbt = playerIn.getHeldItemOffhand().getTagCompound();
						nbt.setBoolean("BUCI",  true);
						playerIn.getHeldItemOffhand().setTagCompound(nbt);
					}

				
			}
		});
		actions.add(new ScrollAction("blank") {
			@Override
			public void onItemRightClick(World worldIn, EntityPlayer playerIn,
					EnumHand handIn) {
				// TODO Auto-generated method stub

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
