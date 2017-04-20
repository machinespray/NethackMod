package com.machinespray.ROYAL;

import java.util.ArrayList;

import scala.Array;
import baubles.api.BaublesApi;

import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import com.machinespray.ROYAL.knowledge.Provider;
import com.machinespray.ROYAL.rings.ItemRing;
import com.machinespray.ROYAL.rings.RingActions;
import com.machinespray.ROYAL.scrolls.ScrollActions;
import com.machinespray.ROYAL.sync.MessageRequestKnowledge;
import com.machinespray.ROYAL.sync.MessageSendKnowledge;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Events implements Constants {
	@SubscribeEvent
	public void onLoad(WorldEvent.Load e) {
		if(!e.getWorld().isRemote){
			RingActions.match(e.getWorld().getSeed());
			ScrollActions.match(e.getWorld().getSeed());
		}
	}

	@SubscribeEvent
	public void onDrop(LivingDropsEvent e) {
		if (Main.random.nextInt(50) < 48) {
			ItemStack stack = new ItemStack(RoyalItems.rings.get(10));
			NBTTagCompound nbt = new NBTTagCompound();
			if (stack.getTagCompound() != null)
				nbt = stack.getTagCompound();
			switch (Main.random.nextInt(10)) {
			case 8:
				nbt.setString("BUC", BLESSED);
				break;
			case 9:
				nbt.setString("BUC", CURSED);
				break;
			default:
				nbt.setString("BUC", UNCURSED);
			}
			stack.setTagCompound(nbt);
			e.getDrops().add(
					new EntityItem(e.getEntity().world, e.getEntity().posX, e
							.getEntity().posY, e.getEntity().posZ, stack));
		}
	}

	@SubscribeEvent
	public void onEntityInteract(EntityInteract e) {
		if (e.getTarget() instanceof EntityVillager
				&& e.getEntityPlayer().getHeldItemMainhand().getItem() instanceof NetHackItem
				&& !e.getWorld().isRemote)
			if (((EntityVillager) e.getTarget()).getProfession() == 2) {
				if (!e.getEntityPlayer().getHeldItemMainhand().getTagCompound()
						.getBoolean("BUCI")) {
					String BUC = (NetHackItem.id(e.getEntityPlayer()
							.getHeldItemMainhand(), 0));
					if (BUC.equals(CURSED))
						e.getEntityPlayer().sendMessage(
								new TextComponentString(TextFormatting.RED
										.toString()
										+ "The priest seems disturbed"));
					if (BUC.equals(UNCURSED))
						e.getEntityPlayer().sendMessage(
								new TextComponentString(TextFormatting.AQUA
										.toString()
										+ "The priest seems unimpressed."));
					if (BUC.equals(BLESSED))
						e.getEntityPlayer().sendMessage(
								new TextComponentString(TextFormatting.GREEN
										.toString()
										+ "The priest seems in awe."));
					e.setCanceled(true);
				}
			}
		if(e.getTarget() instanceof EntityTameable){
			EntityTameable target = (EntityTameable) e.getTarget();
					String BUC = (NetHackItem.id(e.getEntityPlayer()
							.getHeldItemMainhand(), 0));
					if (BUC.equals(CURSED))
						e.getEntityPlayer().sendMessage(
								new TextComponentString(TextFormatting.RED
										.toString()
										+ "The animal's eyes grow wide with fear."));
					if (BUC.equals(UNCURSED)||BUC.equals(BLESSED))
						e.getEntityPlayer().sendMessage(
								new TextComponentString(TextFormatting.AQUA
										.toString()
										+ "The animal doesn't react."));
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
						if (RingActions.getAction((BaublesApi
								.getBaublesHandler(e.getEntityPlayer())
								.getStackInSlot(i).getItem()
								.getUnlocalizedName())).name == "strength") {
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
		if (e.getEntity() instanceof EntityPlayerSP){
			RingActions.initActions();
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

}
