package com.machinespray.ROYAL.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderPotionFactory implements IRenderFactory<EntityPotionRoyal> {

    @Override
    public Render<EntityPotionRoyal> createRenderFor(RenderManager manager) {
        return new RenderSnowball(manager, Items.EXPERIENCE_BOTTLE, Minecraft.getMinecraft().getRenderItem());
    }
}
