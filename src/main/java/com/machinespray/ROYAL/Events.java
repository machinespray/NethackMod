package com.machinespray.ROYAL;

import baubles.api.BaublesApi;
import com.machinespray.ROYAL.items.NetHackItem;
import com.machinespray.ROYAL.items.rings.ItemRing;
import com.machinespray.ROYAL.items.rings.RingAction;
import com.machinespray.ROYAL.items.scrolls.ScrollAction;
import com.machinespray.ROYAL.sync.KnowledgeRequestHandler;
import com.machinespray.ROYAL.sync.MessageRequestKnowledge;
import com.machinespray.ROYAL.sync.knowledge.IKnowledgeHandler;
import com.machinespray.ROYAL.sync.knowledge.Provider;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class Events implements Constants {
    @SideOnly(Side.CLIENT)
    private static boolean message = true;

    @SubscribeEvent
    public void onLoad(WorldEvent.Load e) {
        if (!e.getWorld().isRemote) {
            RingAction.match(e.getWorld().getSeed());
            ScrollAction.match(e.getWorld().getSeed());
        }
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteract e) {
        if (e.getTarget() instanceof EntityVillager
                && e.getEntityPlayer().getHeldItemMainhand().getItem() instanceof NetHackItem
                && !e.getWorld().isRemote)
            if (((EntityVillager) e.getTarget()).getProfession() == 2) {
                if (!e.getEntityPlayer().getHeldItemMainhand().getTagCompound()
                        .getBoolean(BUCI)) {
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
        if (e.getTarget() instanceof EntityTameable) {
            if (e.getEntityPlayer().getHeldItemMainhand() != null)
                if (e.getEntityPlayer().getHeldItemMainhand().getItem() instanceof NetHackItem) {
                    EntityTameable target = (EntityTameable) e.getTarget();
                    String BUC = (NetHackItem.id(e.getEntityPlayer()
                            .getHeldItemMainhand(), 0));
                    if (BUC.equals(CURSED))
                        e.getEntityPlayer()
                                .sendMessage(
                                        new TextComponentString(
                                                TextFormatting.RED.toString()
                                                        + "The animal's eyes grow wide with fear."));
                    if (BUC.equals(UNCURSED) || BUC.equals(BLESSED))
                        e.getEntityPlayer().sendMessage(
                                new TextComponentString(TextFormatting.AQUA
                                        .toString()
                                        + "The animal doesn't react."));
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
                        if (RingAction.getAction((BaublesApi
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


    /*@SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRender(RenderPlayerEvent.Pre e) {
        e.getEntity().setInvisible(true);
        LayerTexture layer = new
                LayerTexture(e.getRenderer(), new ModelSilverfish(), new
                ResourceLocation("textures/entity/silverfish.png"));
        e.getRenderer().addLayer(layer);
    }*/

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {

        if (event.getObject() instanceof EntityPlayer)
            event.addCapability(new ResourceLocation("royal", "knowledge"),
                    new Provider());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof EntityPlayerSP && message) {
            message = false;
            Main.rings = new String[ringNames.length];
            Main.scrolls = new String[scrollNames.length];
            Main.WRAPPER_INSTANCE.sendToServer(new MessageRequestKnowledge());
        }
    }

    @SubscribeEvent
    public void respawn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent e) {
        KnowledgeRequestHandler.sendKnowledge((EntityPlayerMP) e.player);
    }

    @SubscribeEvent
    public void clonePlayer(PlayerEvent.Clone event) {
        final IKnowledgeHandler original = Main.getHandler(event.getOriginal());
        final IKnowledgeHandler clone = Main.getHandler(event.getEntity());
        clone.setKnowledge(original.getKnowledge());
    }


}
