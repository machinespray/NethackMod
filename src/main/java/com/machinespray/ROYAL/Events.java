package com.machinespray.ROYAL;

import baubles.api.BaublesApi;
import com.machinespray.ROYAL.altars.RoyalBlocks;
import com.machinespray.ROYAL.items.NetHackItem;
import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import com.machinespray.ROYAL.knowledge.Provider;
import com.machinespray.ROYAL.randomized.ring.RingActionGroup;
import com.machinespray.ROYAL.randomized.scroll.ScrollActionGroup;
import com.machinespray.ROYAL.items.ItemRing;
import com.machinespray.ROYAL.sync.MessageRequestKnowledge;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class Events implements Constants {
	private static boolean message=true;

	@SubscribeEvent
	public void onLoad(WorldEvent.Load e) {
		if (!e.getWorld().isRemote) {
			RingActionGroup.INSTANCE.match(e.getWorld().getSeed());
			ScrollActionGroup.INSTANCE.match(e.getWorld().getSeed());
		}
	}
	@SubscribeEvent
	public void onDrop(LivingDropsEvent e) {
		if (!e.getEntity().world.isRemote&&!(e.getEntity() instanceof EntityPlayer)) {
			if (Main.random.nextInt(15) > 13) {
				ItemStack stack = null;
				if (Main.random.nextInt(2) == 0) {
					stack = new ItemStack(RoyalItems.rings.get(Main.random
							.nextInt(RoyalItems.rings.size())));
					while (!((NetHackItem) stack.getItem()).hasUse())
						stack = new ItemStack(RoyalItems.rings.get(Main.random
								.nextInt(RoyalItems.rings.size())));
				} else {
					stack = new ItemStack(RoyalItems.scrolls.get(Main.random
							.nextInt(RoyalItems.scrolls.size())));
					while (!((NetHackItem) stack.getItem()).hasUse())
						stack = new ItemStack(
								RoyalItems.scrolls.get(Main.random
										.nextInt(RoyalItems.scrolls.size())));
				}
				NBTTagCompound nbt = new NBTTagCompound();
				if (stack.getTagCompound() != null)
					nbt = stack.getTagCompound();
					nbt.setString(BUC, UNCURSED);

				stack.setTagCompound(nbt);
				e.getDrops().add(
						new EntityItem(e.getEntity().world, e.getEntity().posX,
								e.getEntity().posY, e.getEntity().posZ, stack));
			}
		}
	}

	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {

		if (event.getObject() instanceof EntityPlayer)
			event.addCapability(new ResourceLocation("royal", "knowledge"),
					new Provider());
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void joinWorld(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityPlayerSP&&message) {
			message = false;
			Main.rings = new String[ringNames.length];
			Main.scrolls = new String[scrollNames.length];
			Main.INSTANCE.sendToServer(new MessageRequestKnowledge());
		}
	}

	@SubscribeEvent
	public void clonePlayer(PlayerEvent.Clone event) {

		final IKnowledgeHandler original = Main.getHandler(event.getOriginal());
		final IKnowledgeHandler clone = Main.getHandler(event.getEntity());
		clone.setKnowledge(original.getKnowledge());
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		RoyalBlocks.initBlocks();
		RoyalBlocks.registerBlocks();
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		RoyalItems.initItems();
		RoyalItems.registerItems();
	}
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent e){
		B3DLoader.INSTANCE.addDomain(Main.MODID.toLowerCase());
		OBJLoader.INSTANCE.addDomain(Main.MODID.toLowerCase());
		RoyalItems.registerClient();
		RoyalBlocks.registerClient();
	}

}
