package com.machinespray.ROYAL;

import com.machinespray.ROYAL.items.NetHackItem;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.machinespray.ROYAL.render.RenderGUIEvent.buffer;


public class Events implements Constants {
    //Randomize on World Load
    @SubscribeEvent
    public void onLoad(WorldEvent.Load e) {
        if (!e.getWorld().isRemote) {
            Values.ringInstance.match(e.getWorld().getSeed());
            Values.scrollInstance.match(e.getWorld().getSeed());
        }
    }

    //Handle Priest/Wolf Identification
    @SubscribeEvent
    public void onEntityInteract(EntityInteract e) {
        if (e.getTarget() instanceof EntityVillager
                && e.getEntityPlayer().getHeldItemMainhand().getItem() instanceof NetHackItem) {
            e.setCanceled(true);
            if (((EntityVillager) e.getTarget()).getProfession() == 2) {
                if (!e.getEntityPlayer().getHeldItemMainhand().getTagCompound()
                        .getBoolean(BUCI)) {
                    String BUC = NetHackItem.id(e.getEntityPlayer()
                            .getHeldItemMainhand());
                    if (e.getSide().equals(Side.CLIENT)) {
                        if (BUC.equals(CURSED))
                            buffer.add("The priest seems disturbed");
                        if (BUC.equals(UNCURSED))
                            buffer.add("The priest seems unimpressed");
                        if (BUC.equals(BLESSED))
                            buffer.add("The priest seems to be in awe");
                    }
                }
            }
        }
        if (e.getTarget() instanceof EntityTameable) {
            if (e.getEntityPlayer().getHeldItemMainhand() != null)
                if (e.getEntityPlayer().getHeldItemMainhand().getItem() instanceof NetHackItem) {
                    EntityTameable target = (EntityTameable) e.getTarget();
                    String BUC = (NetHackItem.id(e.getEntityPlayer()
                            .getHeldItemMainhand()));
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

    /* @SubscribeEvent
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
        if (e.getEntity() instanceof EntityPlayerSP) {
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
