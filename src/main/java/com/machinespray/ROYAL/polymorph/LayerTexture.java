package com.machinespray.ROYAL.polymorph;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerTexture<M extends ModelBase, R extends RenderLivingBase> implements LayerRenderer<EntityLivingBase> {
    private static ResourceLocation TEXTURE;
    private final R creeperRenderer;
    private final M model;

    public LayerTexture(R creeperRendererIn, M modelBase, ResourceLocation texture) {
        model = modelBase;
        TEXTURE = texture;
        this.creeperRenderer = creeperRendererIn;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        //boolean flag = entitylivingbaseIn.isInvisible();
        // GlStateManager.depthMask(!flag);
        this.creeperRenderer.bindTexture(TEXTURE);
        // GlStateManager.matrixMode(5890);
        // GlStateManager.loadIdentity();
        // float f = (float)entitylivingbaseIn.ticksExisted + partialTicks;
        // GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
        // GlStateManager.matrixMode(5888);
        //  GlStateManager.enableBlend();
        //  float f1 = 0.5F;
        // GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
        //  GlStateManager.disableLighting();
        //  GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        //  this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
        Minecraft.getMinecraft().entityRenderer.func_191514_d(true);
        this.model.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        //  Minecraft.getMinecraft().entityRenderer.func_191514_d(false);
        //  GlStateManager.matrixMode(5890);
        //  GlStateManager.loadIdentity();
        // GlStateManager.matrixMode(5888);
        //  GlStateManager.enableLighting();
        //  GlStateManager.disableBlend();
        //  GlStateManager.depthMask(flag);
    }

    public boolean shouldCombineTextures() {
        return false;
    }

}