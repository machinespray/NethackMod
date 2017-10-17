package com.machinespray.ROYAL.polymorph;

import com.machinespray.ROYAL.polymorph.players.IPlayerMorph;
import com.machinespray.ROYAL.polymorph.players.PolyMorphClient;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerTexture implements LayerRenderer<EntityLivingBase> {

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entitylivingbaseIn;
            IPlayerMorph morph = PolyPlayerData.getPoly(player);
            if (morph != null) {
                if (!morph.isHumanoid()) {
                    PolyMorphClient morphClient = (PolyMorphClient) morph;
                    ModelBase model = morphClient.getModel();
                    EntityLivingBase entity = morphClient.getEntity();
                    morphClient.getRenderer().bindTexture(morphClient.getTexture());
                    model.isChild = false;
                    if (entity instanceof EntityTameable && !(entity instanceof EntityParrot))
                        ((EntityTameable) entity).setSitting(player.isSneaking());
                    model.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
                    model.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
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