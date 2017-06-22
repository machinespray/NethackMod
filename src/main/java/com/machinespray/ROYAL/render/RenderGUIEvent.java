package com.machinespray.ROYAL.render;

import com.google.common.base.Objects;
import com.machinespray.ROYAL.ConfigHandler;
import com.machinespray.ROYAL.polymorph.PolyPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        if(ConfigHandler.DoCustomArmorRender)
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
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void RenderHandEvent(RenderHandEvent e){
        Minecraft mc = Minecraft.getMinecraft();
        if(ConfigHandler.DoCustomArmRender)
        if(PolyPlayerData.getPoly(mc.player)!=0) {
            e.setCanceled(true);
            boolean flag = mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase) mc.getRenderViewEntity()).isPlayerSleeping();
            if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && !flag && !mc.gameSettings.hideGUI && !mc.playerController.isSpectator()) {
                Minecraft.getMinecraft().entityRenderer.enableLightmap();
                renderItemInFirstPerson(e.getPartialTicks());
                Minecraft.getMinecraft().entityRenderer.disableLightmap();
            }
        }
    }

    //Modified Code from vanilla classes for RenderHandEvent
    @SideOnly(Side.CLIENT)
    private void renderArmFirstPerson(float p_187456_1_, float p_187456_2_, EnumHandSide p_187456_3_)
    {
        boolean flag = p_187456_3_ != EnumHandSide.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(p_187456_2_);
        float f2 = -0.3F * MathHelper.sin(f1 * (float)Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float)Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(p_187456_2_ * (float)Math.PI);
        GlStateManager.translate(f * (f2 + 0.64000005F), f3 + -0.6F + p_187456_1_ * -0.6F, f4 + -0.71999997F);
        GlStateManager.rotate(f * 45.0F, 0.0F, 1.0F, 0.0F);
        float f5 = MathHelper.sin(p_187456_2_ * p_187456_2_ * (float)Math.PI);
        float f6 = MathHelper.sin(f1 * (float)Math.PI);
        GlStateManager.rotate(f * f6 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f * f5 * -20.0F, 0.0F, 0.0F, 1.0F);
        AbstractClientPlayer abstractclientplayer = Minecraft.getMinecraft().player;
        //INSERT NEW TEXTURE
        Minecraft.getMinecraft().getTextureManager().bindTexture(PolyPlayerData.getPolyTexture(Minecraft.getMinecraft().player));
        GlStateManager.translate(f * -1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(f * 120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(f * 5.6F, 0.0F, 0.0F);
        RenderPlayer renderplayer = (RenderPlayer) Minecraft.getMinecraft().getItemRenderer().renderManager.getEntityRenderObject(abstractclientplayer);
        GlStateManager.disableCull();

        if (flag)
        {
            renderplayer.renderRightArm(abstractclientplayer);
        }
        else
        {
            renderplayer.renderLeftArm(abstractclientplayer);
        }

        GlStateManager.enableCull();
    }


    public void renderItemInFirstPerson(float partialTicks)
    {
        Minecraft mc = Minecraft.getMinecraft();
        AbstractClientPlayer abstractclientplayer = mc.player;
        ItemRenderer itemRenderer = mc.getItemRenderer();
        float f = abstractclientplayer.getSwingProgress(partialTicks);
        EnumHand enumhand = (EnumHand) Objects.firstNonNull(abstractclientplayer.swingingHand, EnumHand.MAIN_HAND);
        float f1 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f2 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        boolean flag = true;
        boolean flag1 = true;

        if (abstractclientplayer.isHandActive())
        {
            ItemStack itemstack = abstractclientplayer.getActiveItemStack();

            if (itemstack != null && itemstack.getItem() == Items.BOW) //Forge: Data watcher can desync and cause this to NPE...
            {
                EnumHand enumhand1 = abstractclientplayer.getActiveHand();
                flag = enumhand1 == EnumHand.MAIN_HAND;
                flag1 = !flag;
            }
        }

        this.rotateArroundXAndY(f1, f2);
        this.setLightmap();
        this.rotateArm(partialTicks);
        GlStateManager.enableRescaleNormal();

        if (flag)
        {
            float f3 = enumhand == EnumHand.MAIN_HAND ? f : 0.0F;
            float f5 = 1.0F - (itemRenderer.prevEquippedProgressMainHand + (itemRenderer.equippedProgressMainHand - itemRenderer.prevEquippedProgressMainHand) * partialTicks);
            if(!net.minecraftforge.client.ForgeHooksClient.renderSpecificFirstPersonHand(EnumHand.MAIN_HAND, partialTicks, f1, f3, f5, itemRenderer.itemStackMainHand))
                this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.MAIN_HAND, f3, itemRenderer.itemStackMainHand, f5);
        }

        if (flag1)
        {
            float f4 = enumhand == EnumHand.OFF_HAND ? f : 0.0F;
            float f6 = 1.0F - (itemRenderer.prevEquippedProgressOffHand + (itemRenderer.equippedProgressOffHand - itemRenderer.prevEquippedProgressOffHand) * partialTicks);
            if(!net.minecraftforge.client.ForgeHooksClient.renderSpecificFirstPersonHand(EnumHand.OFF_HAND, partialTicks, f1, f4, f6, itemRenderer.itemStackOffHand))
                this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.OFF_HAND, f4, itemRenderer.itemStackOffHand, f6);
        }

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }
    private void rotateArroundXAndY(float angle, float angleY)
    {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(angleY, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void setLightmap()
    {
        Minecraft mc = Minecraft.getMinecraft();
        AbstractClientPlayer abstractclientplayer = mc.player;
        int i = mc.world.getCombinedLight(new BlockPos(abstractclientplayer.posX, abstractclientplayer.posY + (double)abstractclientplayer.getEyeHeight(), abstractclientplayer.posZ), 0);
        float f = (float)(i & 65535);
        float f1 = (float)(i >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
    }

    private void rotateArm(float p_187458_1_)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP entityplayersp = mc.player;
        float f = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * p_187458_1_;
        float f1 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * p_187458_1_;
        GlStateManager.rotate((entityplayersp.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((entityplayersp.rotationYaw - f1) * 0.1F, 0.0F, 1.0F, 0.0F);
    }

    public void renderItemInFirstPerson(AbstractClientPlayer p_187457_1_, float p_187457_2_, float p_187457_3_, EnumHand p_187457_4_, float p_187457_5_, ItemStack p_187457_6_, float p_187457_7_)
    {
        ItemRenderer itemRenderer = Minecraft.getMinecraft().getItemRenderer();
        boolean flag = p_187457_4_ == EnumHand.MAIN_HAND;
        EnumHandSide enumhandside = flag ? p_187457_1_.getPrimaryHand() : p_187457_1_.getPrimaryHand().opposite();
        GlStateManager.pushMatrix();

        if (p_187457_6_.isEmpty())
        {
            if (flag && !p_187457_1_.isInvisible())
            {
                this.renderArmFirstPerson(p_187457_7_, p_187457_5_, enumhandside);
            }
        }
        else if (p_187457_6_.getItem() instanceof net.minecraft.item.ItemMap)
        {
            if (flag && itemRenderer.itemStackOffHand.isEmpty())
            {
                //itemRenderer.renderMapFirstPerson(p_187457_3_, p_187457_7_, p_187457_5_);
            }
            else
            {
                //this.renderMapFirstPersonSide(p_187457_7_, enumhandside, p_187457_5_, p_187457_6_);
            }
        }
        else
        {
            boolean flag1 = enumhandside == EnumHandSide.RIGHT;

            if (p_187457_1_.isHandActive() && p_187457_1_.getItemInUseCount() > 0 && p_187457_1_.getActiveHand() == p_187457_4_)
            {
                int j = flag1 ? 1 : -1;

                switch (p_187457_6_.getItemUseAction())
                {
                    case NONE:
                        itemRenderer.transformSideFirstPerson(enumhandside, p_187457_7_);
                        break;
                    case EAT:
                    case DRINK:
                        itemRenderer.transformEatFirstPerson(p_187457_2_, enumhandside, p_187457_6_);
                        itemRenderer.transformSideFirstPerson(enumhandside, p_187457_7_);
                        break;
                    case BLOCK:
                        itemRenderer.transformSideFirstPerson(enumhandside, p_187457_7_);
                        break;
                    case BOW:
                        itemRenderer.transformSideFirstPerson(enumhandside, p_187457_7_);
                        GlStateManager.translate((float)j * -0.2785682F, 0.18344387F, 0.15731531F);
                        GlStateManager.rotate(-13.935F, 1.0F, 0.0F, 0.0F);
                        GlStateManager.rotate((float)j * 35.3F, 0.0F, 1.0F, 0.0F);
                        GlStateManager.rotate((float)j * -9.785F, 0.0F, 0.0F, 1.0F);
                        float f5 = (float)p_187457_6_.getMaxItemUseDuration() - ((float)Minecraft.getMinecraft().player.getItemInUseCount() - p_187457_2_ + 1.0F);
                        float f6 = f5 / 20.0F;
                        f6 = (f6 * f6 + f6 * 2.0F) / 3.0F;

                        if (f6 > 1.0F)
                        {
                            f6 = 1.0F;
                        }

                        if (f6 > 0.1F)
                        {
                            float f7 = MathHelper.sin((f5 - 0.1F) * 1.3F);
                            float f3 = f6 - 0.1F;
                            float f4 = f7 * f3;
                            GlStateManager.translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
                        }

                        GlStateManager.translate(f6 * 0.0F, f6 * 0.0F, f6 * 0.04F);
                        GlStateManager.scale(1.0F, 1.0F, 1.0F + f6 * 0.2F);
                        GlStateManager.rotate((float)j * 45.0F, 0.0F, -1.0F, 0.0F);
                }
            }
            else
            {
                float f = -0.4F * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * (float)Math.PI);
                float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * ((float)Math.PI * 2F));
                float f2 = -0.2F * MathHelper.sin(p_187457_5_ * (float)Math.PI);
                int i = flag1 ? 1 : -1;
                GlStateManager.translate((float)i * f, f1, f2);
                itemRenderer.transformSideFirstPerson(enumhandside, p_187457_7_);
                itemRenderer.transformFirstPerson(enumhandside, p_187457_5_);
            }

            itemRenderer.renderItemSide(p_187457_1_, p_187457_6_, flag1 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag1);
        }

        GlStateManager.popMatrix();
    }


}
