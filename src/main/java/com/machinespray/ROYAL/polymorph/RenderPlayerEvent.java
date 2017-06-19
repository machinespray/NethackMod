package com.machinespray.ROYAL.polymorph;

import baubles.api.BaublesApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class RenderPlayerEvent {
    private static LayerTexture zombieLayer;
    public static ArrayList<EntityLivingBase> toOutline = new ArrayList<EntityLivingBase>();

    public static void init() {
        ModelZombie zombie = new ModelZombie();
        ResourceLocation zombieTexture = new
                ResourceLocation("textures/entity/zombie/zombie.png");
        zombie.isChild = false;
        zombieLayer = new
                LayerTexture(new RenderZombie(Minecraft.getMinecraft().getRenderManager()), zombie, zombieTexture);
        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("slim").addLayer(zombieLayer);
        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("default").addLayer(zombieLayer);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRender(net.minecraftforge.client.event.RenderLivingEvent.Pre e) {
        if(toOutline.contains(e.getEntity())) {
            e.getRenderer().setRenderOutlines(true);
            toOutline.remove(e.getEntity());
        }else{
            e.getRenderer().setRenderOutlines(false);
        }
    }


}
