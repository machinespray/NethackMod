package com.machinespray.ROYAL.polymorph;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerTexture implements LayerRenderer<EntityLivingBase> {

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entitylivingbaseIn;
            if (PolyPlayerData.getPoly(player) != 0) {
                ModelBase model = PolyPlayerData.getPolyModel(player);
                //model.isChild = false;
                PolyPlayerData.getPolyRenderer(player).bindTexture(PolyPlayerData.getPolyTexture(player));
                //Minecraft.getMinecraft().entityRenderer.func_191514_d(true);
                if (model instanceof ModelPlayer) {
                    if (entitylivingbaseIn.isSneaking()) {
                        ((ModelPlayer) model).isSneak = true;

                    } else {
                        ((ModelPlayer) model).isSneak = false;

                    }
                }
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                model.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            }
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }

}