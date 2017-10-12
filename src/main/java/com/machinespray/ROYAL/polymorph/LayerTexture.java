package com.machinespray.ROYAL.polymorph;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.machinespray.ROYAL.polymorph.PolyPlayerData.NONE;

@SideOnly(Side.CLIENT)
public class LayerTexture implements LayerRenderer<EntityLivingBase> {

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entitylivingbaseIn;
            if (PolyPlayerData.getPoly(player) != NONE) {
                if (PolyPlayerData.getPolyEntity(player) == null) {
                    ModelBase model = PolyPlayerData.getPolyModel(player);
                    model.isChild = false;
                    PolyPlayerData.getPolyRenderer(player).bindTexture(PolyPlayerData.getPolyTexture(player));
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
                } else {
                    ModelBase model = PolyPlayerData.getPolyModel(player);
                    EntityLivingBase entity = PolyPlayerData.getPolyEntity(player);
                    PolyPlayerData.getPolyRenderer(player).bindTexture(PolyPlayerData.getPolyTexture(player));
                    model.isChild = false;
                    if (entity instanceof EntityTameable&&!(entity instanceof EntityParrot))
                        ((EntityTameable) entity).setSitting(player.isSneaking());
                    model.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
                    model.setRotationAngles(limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scale,entity);
                    if (entity instanceof EntityWolf) {
                        model.render(entity, limbSwing, limbSwingAmount, ((EntityWolf) entity).getTailRotation(), netHeadYaw, headPitch, scale);
                    } else {
                        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    }
                }
            }
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }

}