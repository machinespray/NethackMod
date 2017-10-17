package com.machinespray.ROYAL.asm;

import com.machinespray.ROYAL.polymorph.PolyEvents;
import com.machinespray.ROYAL.polymorph.PolyPlayerData;
import com.machinespray.ROYAL.polymorph.players.IPlayerMorph;
import com.machinespray.ROYAL.polymorph.players.PlayerMorphClient;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AsmHooks {

    public static float eyeHeightHook(float eyeHeight, EntityPlayer player) {
        IPlayerMorph morph = PolyPlayerData.getPoly(player);
        if (morph != null)
            if (!morph.isHumanoid())
                return (float) (morph.getEyeHeight() * getScale(player));
        return (eyeHeight);
    }

    @SideOnly(Side.CLIENT)
    public static boolean layerRenderHook(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn) {
        if (entitylivingbaseIn instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer) entitylivingbaseIn;
            IPlayerMorph morph = PolyPlayerData.getPoly(p);
            if (morph != null)
                if (!morph.isHumanoid()) {
                    PolyEvents.texture.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn);
                    return false;
                }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public static ResourceLocation getSkin(ResourceLocation l, AbstractClientPlayer p) {
        try {
            IPlayerMorph morph = PolyPlayerData.getPoly(p);
            if (morph != null) {
                if (morph.isHumanoid()) {
                    PlayerMorphClient morphClient = (PlayerMorphClient) morph;
                    ClientHooks.getSkinType().set(ClientHooks.getPlayerInfo().get(p), "default");
                    return morphClient.getTexture();
                }
            } else {
                    ClientHooks.getSkinType().set(ClientHooks.getPlayerInfo().get(p), DefaultPlayerSkin.getSkinType(p.getUniqueID()));

            }
        } catch (Exception e) {
            e.printStackTrace();
            //return l;
        }
        return l;
    }

    public static double getScale(Entity e) {
        if (Loader.isModLoaded("lilliputian"))
            return getModdedScale(e);
        return 1.0D;
    }

    @Optional.Method(modid = "lilliputian")
    public static double getModdedScale(Entity e) {
        return lilliputian.util.EntitySizeUtil.getEntityScale(e);
    }

}
