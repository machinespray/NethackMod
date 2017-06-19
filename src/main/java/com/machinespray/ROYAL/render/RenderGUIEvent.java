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

import static net.minecraftforge.client.GuiIngameForge.left_height;

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
                    double boxheight = top - bottom;
                    double percent;
                    boolean temp = true;
                    if (time < 120) {
                        percent = boxheight * (time / 120D);
                    } else if (time < 840) {
                        percent = boxheight;
                    } else if (time < 960) {
                        percent = boxheight * (1 - ((time - 840) / 120D));
                    } else {
                        percent = 0;
                        time = 0;
                        temp = false;
                    }
                    if (temp) {
                        Tessellator tessellator = Tessellator.getInstance();
                        VertexBuffer vertexbuffer = tessellator.getBuffer();
                        GlStateManager.disableBlend();
                        GlStateManager.enableTexture2D();
                        minecraft.getTextureManager().bindTexture(new ResourceLocation("textures/gui/achievement/achievement_background.png"));
                        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
                        vertexbuffer.pos(left, bottom, 0.0D).tex(1, .79).endVertex();
                        vertexbuffer.pos(right, bottom, 0.0D).tex(.37, .79).endVertex();
                        vertexbuffer.pos(right, top, 0.0D).tex(.37, .91).endVertex();
                        vertexbuffer.pos(left, top, 0.0D).tex(1, .91).endVertex();
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(0D, boxheight - percent, 0F);
                        tessellator.draw();
                        GlStateManager.popMatrix();
                        GlStateManager.pushMatrix();
                        GlStateManager.scale(.5, .5, 1);
                        String[] phrases = buffer.get(0).split("\n");
                        GlStateManager.translate(width, (1.0025 * height) - ((Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * phrases.length) / 2), 0);
                        GlStateManager.translate(0D, (boxheight - percent) * 2, 0F);
                        for (int i = 0; i < phrases.length; i++) {
                            Minecraft.getMinecraft().fontRendererObj.drawString(phrases[i], (int) Math.floor(right) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(phrases[0]) / 2, (int) Math.floor(bottom) + (i * (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT)), 0xC1C1C1);
                        }
                        GlStateManager.popMatrix();
                    } else {
                        buffer.remove(0);
                        time = 0;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void renderArmor(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ARMOR) {
            event.setCanceled(true);
            Minecraft mc = Minecraft.getMinecraft();
            mc.mcProfiler.startSection("armor");
            GlStateManager.enableBlend();
            int left = event.getResolution().getScaledWidth() / 2 - 91;
            int top = event.getResolution().getScaledHeight() - left_height;

            int level = mc.player.getTotalArmorValue();
            for (int i = 1; level > 0 && i < 20; i += 2) {
                if (i < level) {
                    drawTexturedModalRect(left, top, 34, 9, 9, 9);
                } else if (i == level) {
                    drawTexturedModalRect(left, top, 25, 9, 9, 9);
                } else if (i > level) {
                    drawTexturedModalRect(left, top, 16, 9, 9, 9);
                }
                left += 8;
            }
            left_height += 10;
            GlStateManager.disableBlend();
            mc.mcProfiler.endSection();
        }
    }

}
