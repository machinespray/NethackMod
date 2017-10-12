package com.machinespray.ROYAL.asm;

import com.machinespray.ROYAL.polymorph.PolyEvents;
import com.machinespray.ROYAL.polymorph.PolyPlayerData;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

public class AsmHooks {

    public static float eyeHeightHook(float eyeHeight, EntityPlayer player) {
        player.setSneaking(false);
        player.setFlag(7,false);
        if (PolyPlayerData.getPoly(player) != 0)
            if (!(PolyPlayerData.getPolyModel(player) instanceof ModelPlayer))
                return (float) ((0.9 * PolyPlayerData.getPolySize(player))*getScale(player));
        return eyeHeight;
    }
    public static double getScale(Entity e){
        if(Loader.isModLoaded("lilliputian"))
        return getModdedScale(e);
        return 1.0D;
    }
    @Optional.Method(modid = "lilliputian")
    public static double getModdedScale(Entity e){
        return lilliputian.util.EntitySizeUtil.getEntityScale(e);
    }

    public static boolean layerRenderHook(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn) {
        if (entitylivingbaseIn instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer) entitylivingbaseIn;
            if (PolyPlayerData.getPoly(p) != 0)
                if (!(PolyPlayerData.getPolyModel(p) instanceof ModelPlayer)) {
                    PolyEvents.texture.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn);
                    return false;
                }
        }
        return true;
    }
}
