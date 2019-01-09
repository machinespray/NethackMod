package com.machinespray.royal;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.machinespray.royal.action.rings.RingAction;
import com.machinespray.royal.action.rings.RingName;
import com.machinespray.royal.action.scrolls.ScrollAction;
import com.machinespray.royal.knowledge.DefaultKnowledgeHandler;
import com.machinespray.royal.knowledge.IKnowledgeHandler;
import com.machinespray.royal.knowledge.Provider;
import com.machinespray.royal.sync.MessageRequestKnowledge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class Events implements Constants {
	private static boolean worldKnowledge = false;
	//Tasks used to delay getting knowledge when joining a server
	private static Timer timer = new Timer();
	private static TimerTask scheduleGetKnowledge;
	private static List<UUID> clientVisionList = new ArrayList<>();

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

	//Handle the ring of vision effect
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void renderEntities(RenderLivingEvent.Pre e) {
		EntityLivingBase entity = e.getEntity();
		UUID uuid = entity.getUniqueID();
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		boolean vision = false;
		for (int i = 0; i < baubles.getSlots(); i++) {
			Item item = baubles.getStackInSlot(i).getItem();
			if (!(item instanceof NetHackItem))
				continue;
			String use = ((NetHackItem) item).getUse();
			vision = use.equals(RingAction.VISION.getKnowledgeName()) || vision;
		}
		if (!vision) {
			for (UUID uuid1 : clientVisionList) {
				Entity entity1 = getEntityFromLoaded(uuid1);
				if (entity1 != null)
					entity1.setGlowing(false);
			}
			clientVisionList.clear();
			return;
		}
		if (!player.equals(entity)) {
			if (player.getDistanceSqToEntity(entity) > 150) {
				if (clientVisionList.contains(uuid)) {
					entity.setGlowing(false);
					clientVisionList.remove(uuid);
				}
				return;
			}
			clientVisionList.add(uuid);
			e.getEntity().setGlowing(true);
		}
	}

	@SideOnly(Side.CLIENT)
	private static Entity getEntityFromLoaded(UUID uuid1) {
		List<Entity> entities = Minecraft.getMinecraft().world.getLoadedEntityList();
		for (Entity e : entities)
			if (e.getUniqueID().equals(uuid1))
				return e;
		return null;
	}

	@SubscribeEvent
	public static void anvilUpdate(AnvilUpdateEvent e) {
		if (!isRing(e.getLeft()) || getHarvest(e.getRight(), "axe") < 2)
			return;
		e.setMaterialCost(1);
		e.setCost(1);
		NetHackItem nhi = (NetHackItem) e.getLeft().getItem();
		String name = RingName.values()[nhi.getID()].getOreDictName();
		NonNullList ores = OreDictionary.getOres(name);
		if (ores.size() > 0) {
			e.setOutput(OreDictionary.getOres(name).get(0).copy());
			return;
		}
		e.setOutput(new ItemStack(Items.IRON_NUGGET));
	}

	@SubscribeEvent
	public static void anvilFinish(AnvilRepairEvent e) {
		//Continue only if this is a ring being smashed
		if (!isRing(e.getItemInput()))
			return;
		EntityLivingBase entityLiving = e.getEntityLiving();
		World world = entityLiving.world;
		//Continue only on the server
		if (world.isRemote)
			return;
		BlockPos pos = e.getEntityLiving().getPosition();
		ItemStack axe = e.getIngredientInput();
		//Try to damage the axe used, if possible
		if (axe.isItemStackDamageable())
			axe.damageItem(5, entityLiving);
		//Play a sound hint, if applicable
		NetHackItem nhi = (NetHackItem) e.getItemInput().getItem();
		RingAction action = RingAction.getAction(nhi.getUnlocalizedName());
		if (action != null)
			world.playSound(null, pos, action.hint, SoundCategory.PLAYERS, 3.0F, 1.0F);
		//Give the axe back
		EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), axe);
		world.spawnEntity(entityItem);
		if (!hasOres(nhi))
			return;
		//Drop an Iron Ingot, if the anvil didn't already give the player one.
		entityItem = new EntityItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), new ItemStack(Items.IRON_NUGGET));
		world.spawnEntity(entityItem);
	}

	private static boolean isRing(ItemStack i) {
		if (!(i.getItem() instanceof NetHackItem))
			return false;
		NetHackItem nhi = (NetHackItem) i.getItem();
		if (nhi.type().equals("ring"))
			return true;
		return false;
	}

	private static boolean hasOres(NetHackItem nhi) {
		String name = RingName.values()[nhi.getID()].getOreDictName();
		NonNullList ores = OreDictionary.getOres(name);
		return ores.size() > 0;
	}

	private static int getHarvest(ItemStack i, String toolClass) {
		return i.getItem().getHarvestLevel(i, toolClass, null, null);
	}

	@SubscribeEvent
	public void onDrop(LivingDropsEvent e) {
		if (Main.config.doMobDrops)
			if (!e.getEntity().world.isRemote && !(e.getEntity() instanceof EntityPlayer)) {
				if (Main.random.nextInt(15) > 13) {
					ItemStack stack = Main.getStackForWorld();
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
		if (!(e.getEntity() instanceof EntityPlayerSP) || worldKnowledge || Main.proxy.getPlayer() == null)
			return;
		if (!e.getEntity().equals(Main.proxy.getPlayer()))
			return;
		worldKnowledge = true;
		RingAction.ids.clear();
		for (RingAction ignored : RingAction.values())
			RingAction.ids.add(-1);
		ScrollAction.ids.clear();
		for (ScrollAction ignored : ScrollAction.values())
			ScrollAction.ids.add(-1);

		Main.clientKnowledge = new DefaultKnowledgeHandler();
		System.out.println("Knowledge Request Sent!");
		Main.INSTANCE.sendToServer(new MessageRequestKnowledge());
	}

	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void clonePlayer(PlayerEvent.Clone event) {
		final IKnowledgeHandler original = Main.getHandler(event.getOriginal());
		final IKnowledgeHandler clone = Main.getHandler(event.getEntityPlayer());
		clone.setKnowledge(original.getKnowledge());
	}
}
