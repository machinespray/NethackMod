package com.machinespray.ROYAL.polymorph;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class RenderPlayerEvent {
    private static LayerTexture zombieLayer;
    private static ResourceLocation zombieTexture;
    public static ArrayList<EntityLivingBase> toOutline = new ArrayList<EntityLivingBase>();

    public static void init() {
        ModelPlayer zombie = new ModelPlayer(0.0F,false);
        zombieTexture = new
                ResourceLocation("royal","textures/polymorph/zombie.png");
        zombie.isChild = false;
        zombieLayer = new
                LayerTexture(new RenderPlayer(Minecraft.getMinecraft().getRenderManager()), zombie, zombieTexture);
        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("slim").addLayer(zombieLayer);
        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("default").addLayer(zombieLayer);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRender(net.minecraftforge.client.event.RenderLivingEvent.Pre e) {
        //Ring of See Invisible
        if (toOutline.contains(e.getEntity())) {
            e.getRenderer().setRenderOutlines(true);
            toOutline.remove(e.getEntity());
        } else {
            e.getRenderer().setRenderOutlines(false);
        }
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRender(net.minecraftforge.client.event.RenderPlayerEvent.Pre e) {
        if(PolyPlayerData.zombies.contains(e.getEntityPlayer().getUniqueID().toString()))
        e.getEntityPlayer().setInvisible(true);
    }


}
