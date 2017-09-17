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
	private static EntityPlayer player;
	public static String[] gods = { "Athena.Hermes.Poseidon",
			"Mercury.Venus.Mars", "Tyr.Odin.Loki", "Ptah.Thoth.Set" };

	@SubscribeEvent
	public void onLoad(WorldEvent.Load e) {
		if (!e.getWorld().isRemote) {
			RingActionGroup.INSTANCE.match(e.getWorld().getSeed());
			ScrollActionGroup.INSTANCE.match(e.getWorld().getSeed());
		}
	}
public static String[] getGods(long seed,String[] godsList){
	Random random = new Random();
	random.setSeed(seed);
	String gods[] = new String[3];
	gods[0] = godsList[random.nextInt(godsList.length)];
	gods[2] = gods[0].split("\\.")[2];
	gods[1] = gods[0].split("\\.")[1];
	gods[0] = gods[0].split("\\.")[0];
	return gods;
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
	public void onBlockBreakBegin(PlayerEvent.BreakSpeed e) {
		World world = e.getEntityPlayer().world;
		if (e.getEntityPlayer().world
				.getBlockState(e.getPos())
				.getBlock()
				.getBlockHardness(
						e.getEntityPlayer().world.getBlockState(e.getPos()),
						e.getEntityPlayer().world, e.getPos()) < .7
				&& e.getEntityPlayer().world
						.getBlockState(e.getPos())
						.getBlock()
						.getBlockHardness(
								e.getEntityPlayer().world.getBlockState(e
										.getPos()), e.getEntityPlayer().world,
								e.getPos()) > 0)
			if (!world.isRemote)
				for (int i = 0; i < BaublesApi.getBaublesHandler(
						e.getEntityPlayer()).getSlots(); i++) {
					if (BaublesApi.getBaublesHandler(e.getEntityPlayer())
							.getStackInSlot(i).getItem() instanceof ItemRing)
						if (RingActionGroup.INSTANCE.getAction((BaublesApi
								.getBaublesHandler(e.getEntityPlayer())
								.getStackInSlot(i).getItem()
								.getUnlocalizedName())).getKnowledgeName() == "strength") {
							if (e.getEntityPlayer().inventory
									.addItemStackToInventory(e
											.getEntityPlayer().world
											.getBlockState(e.getPos())
											.getBlock()
											.getItem(
													e.getEntityPlayer().world,
													e.getPos(),
													e.getEntityPlayer().world
															.getBlockState(e
																	.getPos())))) {
								e.getEntityPlayer().world.setBlockState(
										e.getPos(),
										Blocks.AIR.getDefaultState());
							}
						}
				}
	}

	/*
	 * @SideOnly(Side.CLIENT)
	 * 
	 * @SubscribeEvent public void onRender(RenderPlayerEvent.Pre e){
	 * e.getEntity().setInvisible(true); LayerTexture layer = new
	 * LayerTexture(e.getRenderer(), new ModelSquid(),new
	 * ResourceLocation("textures/entity/squid.png"));
	 * e.getRenderer().addLayer(layer); }
	 */

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
			e.getEntity()
					.sendMessage(
							new TextComponentString(
									"Thank you for using ROYAL "
											+ Main.VERSION
											+ ".\n In order to offer to a god, type\"#offer\", with at least 9 gold nuggets in hand."));
		}
	}

	@SubscribeEvent
	public void clonePlayer(PlayerEvent.Clone event) {

		final IKnowledgeHandler original = Main.getHandler(event.getOriginal());
		final IKnowledgeHandler clone = Main.getHandler(event.getEntity());
		clone.setKnowledge(original.getKnowledge());
	}

	@SubscribeEvent
	public void onChat(ServerChatEvent e) {
		String[] gods = getGods(e.getPlayer().world.getSeed(),this.gods);
		if (e.getMessage().equals("#offer") && e.getPlayer() != player
				&& e.getPlayer().getHeldItemMainhand() != null)
			if (e.getPlayer().getHeldItemMainhand().getItem()
					.equals(Items.GOLD_NUGGET)
					&& e.getPlayer().getHeldItemMainhand().getCount() > 8) {
				e.getPlayer()
						.getHeldItemMainhand()
						.setCount(
								e.getPlayer().getHeldItemMainhand().getCount() - 9);
				e.getPlayer().sendMessage(
						new TextComponentString("Offer To Whom?\na). "
								+ gods[0] + " (Lawful)\nb). " + gods[1]
								+ " (Neutral)\nc). " + gods[2] + " (Chaotic)"));
				if (player != null)
					player.sendMessage(new TextComponentString(
							"The gods have are now listening to someone else."));
				player = e.getPlayer();
				e.setCanceled(true);
			}
		if (e.getPlayer().equals(player) && !e.getMessage().equals("#offer")) {
			e.setCanceled(true);
			if (e.getMessage().equals("a") || e.getMessage().equals("b")
					|| e.getMessage().equals("c")) {
				int to = Character
						.getNumericValue(e.getMessage().charAt(0) - 49);
				player.sendMessage(new TextComponentString(gods[to]
						+ " hears your prayer!"));
				if (Main.random.nextInt(4) == 0) {
					ItemStack stack = new ItemStack(RoyalBlocks.altars.get(to));
					if (player.inventory.addItemStackToInventory(stack))
						player.sendMessage(new TextComponentString(
								gods[to]
										+ " grants you an altar of their allignment. Sacrificing gold bars at it can grant you rewards!"));

				}

			} else {
				player.sendMessage(new TextComponentString(
						"The gods do not wish to hear from you now."));
			}
			player = null;
		}
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
