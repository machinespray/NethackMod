package com.machinespray.ROYAL.polymorph;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerTexture<M extends ModelBase, R extends RenderLivingBase> implements LayerRenderer<EntityLivingBase> {
    private static ResourceLocation TEXTURE;
    private final R renderer;
    private final M model;

    public LayerTexture(R rendererIn, M modelBase, ResourceLocation texture) {
        model = modelBase;
        TEXTURE = texture;
        this.renderer = rendererIn;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (PolyPlayerData.zombies.contains(entitylivingbaseIn.getUniqueID().toString())) {
            this.renderer.bindTexture(TEXTURE);
            Minecraft.getMinecraft().entityRenderer.func_191514_d(true);
            if (model instanceof ModelPlayer) {
                if (entitylivingbaseIn.isSneaking()) {
                    ((ModelPlayer) model).isSneak = true;

                } else {
                    ((ModelPlayer) model).isSneak = false;

                }
            }
            this.model.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }

}