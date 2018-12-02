package com.machinespray.ROYAL;

import com.machinespray.ROYAL.knowledge.DefaultKnowledgeHandler;
import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import com.machinespray.ROYAL.knowledge.Provider;
import com.machinespray.ROYAL.rings.RingAction;
import com.machinespray.ROYAL.scrolls.ScrollAction;
import com.machinespray.ROYAL.sync.MessageRequestKnowledge;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.network.ForgeNetworkHandler;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class Events implements Constants {
	private static boolean worldKnowledge = false;

	@SubscribeEvent
	public void onDrop(LivingDropsEvent e) {
		if (!e.getEntity().world.isRemote && !(e.getEntity() instanceof EntityPlayer)) {
			if (Main.random.nextInt(15) > 13) {
				ItemStack stack;
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

	@SubscribeEvent
	public void joinWorldClient(WorldEvent.Load e) {
		if (e.getWorld().isRemote) {
			resetSchedule();
			worldKnowledge = false;
			timer.scheduleAtFixedRate(scheduleGetKnowledge, 100, 100);
			return;
		}
		RingAction.match(e.getWorld().getSeed());
		ScrollAction.match(e.getWorld().getSeed());
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void joinWorld(EntityJoinWorldEvent e) {
		if ((!(e.getEntity() instanceof EntityPlayerSP) || worldKnowledge) || Main.proxy.getPlayer() == null)
			return;
		if(!e.getEntity().equals(Main.proxy.getPlayer()))
			return;
		worldKnowledge = true;
		RingAction.ids.clear();
		for (RingAction ignored : RingAction.values())
			RingAction.ids.add(-1);
		ScrollAction.ids.clear();
		for (ScrollAction ignored : ScrollAction.values())
			ScrollAction.ids.add(-1);

		Main.clientKnowledge = new DefaultKnowledgeHandler();
		Main.INSTANCE.sendToServer(new MessageRequestKnowledge());
	}

	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void clonePlayer(PlayerEvent.Clone event) {
		final IKnowledgeHandler original = Main.getHandler(event.getOriginal());
		final IKnowledgeHandler clone = Main.getHandler(event.getEntityPlayer());
		clone.setKnowledge(original.getKnowledge());
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		RoyalItems.initItems();
		RoyalItems.registerItems();
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent e) {
		B3DLoader.INSTANCE.addDomain(Main.MODID.toLowerCase());
		OBJLoader.INSTANCE.addDomain(Main.MODID.toLowerCase());
		RoyalItems.registerClient();
	}

	//Tasks used to delay getting knowledge when joining a server
	//TODO find a good place to run this on the client without using a Scheduled thing
	static Timer timer = new Timer();
	static TimerTask scheduleGetKnowledge;

	private static void resetSchedule() {
		scheduleGetKnowledge = new TimerTask() {
			@Override
			public void run() {
				EntityPlayer p = Main.proxy.getPlayer();
				if (p != null) {
					Main.getEventsInstance().joinWorld(new EntityJoinWorldEvent(p, p.world));
					scheduleGetKnowledge.cancel();
				}
			}
		};
	}
}
