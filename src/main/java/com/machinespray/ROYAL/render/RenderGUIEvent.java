package com.machinespray.ROYAL.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class RenderGUIEvent extends Gui {
    public static ArrayList<String> buffer = new ArrayList<String>();
    int time = 0;

    @SubscribeEvent
    public void renderHud(Post event) {
        if (!buffer.isEmpty()) {
            time++;
            Minecraft minecraft = Minecraft.getMinecraft();
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (player.isCreative()) {
                ScaledResolution resolution = event.getResolution();
                double width = resolution.getScaledWidth();
                double height = resolution.getScaledHeight();
                if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
                    Tessellator tessellator = Tessellator.getInstance();
                    VertexBuffer vertexbuffer = tessellator.getBuffer();
                    GlStateManager.disableBlend();
                    GlStateManager.enableTexture2D();
                    minecraft.getTextureManager().bindTexture(new ResourceLocation("textures/gui/achievement/achievement_background.png"));
                    vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
                    double left;
                    double right;
                    double top;
                    double bottom;
                    if (width / height > 16 / 9) {
                        left = width;
                        top = height;
                        bottom = height * (14.7 / 16.0);
                        right = width - ((height - bottom) * 4);
                    } else {
                        left = width;
                        top = height;
                        right = width * (13.0 / 16.0);
                        bottom = height - ((left - right) / 4);
                    }
                    //GlStateManager.alphaFunc();
                    vertexbuffer.pos(left, bottom, 0.0D).tex(1, .79).endVertex();
                    vertexbuffer.pos(right, bottom, 0.0D).tex(.37, .79).endVertex();
                    vertexbuffer.pos(right, top, 0.0D).tex(.37, .91).endVertex();
                    vertexbuffer.pos(left, top, 0.0D).tex(1, .91).endVertex();
                    tessellator.draw();
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(.5, .5, 1);
                    GlStateManager.translate(width - Minecraft.getMinecraft().fontRendererObj.getStringWidth(buffer.get(0)) / 2, height - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2, 0);
                    Minecraft.getMinecraft().fontRendererObj.drawString(buffer.get(0), (int) Math.floor(right), (int) Math.floor(bottom), 0xC1C1C1);
                    GlStateManager.popMatrix();
                }
            }
        }
    }
}
