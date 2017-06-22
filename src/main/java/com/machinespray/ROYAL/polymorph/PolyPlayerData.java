package com.machinespray.ROYAL.polymorph;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class PolyPlayerData {
    public static HashMap<String, Integer> polymorphs = new HashMap<String, Integer>();
    @SideOnly(Side.CLIENT)
    private static HashMap<Integer, ModelBase> polyModel = new HashMap<Integer, ModelBase>();
    @SideOnly(Side.CLIENT)
    private static HashMap<Integer, ResourceLocation> polyTexture = new HashMap<Integer, ResourceLocation>();
    @SideOnly(Side.CLIENT)
    private static HashMap<Integer, RenderLivingBase> polyRenderer = new HashMap<Integer, RenderLivingBase>();
    private static int id = 1;
    public static int NONE = 0;
    public static int ZOMBIE;
    public static int WRAITH;

    public static int registerPoly() {
        return id++;
    }

    @SideOnly(Side.CLIENT)
    public static int registerPoly(ModelBase model, ResourceLocation texture, RenderLivingBase renderer) {
        polyModel.put(id, model);
        polyTexture.put(id, texture);
        polyRenderer.put(id, renderer);
        return registerPoly();
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        ZOMBIE = registerPoly(new ModelPlayer(0.0F, false), new
                ResourceLocation("royal", "textures/polymorph/zombie.png"), new RenderPlayer(Minecraft.getMinecraft().getRenderManager())
        );
        WRAITH = registerPoly(new ModelPlayer(0.0F, false), new
                ResourceLocation("royal", "textures/polymorph/wraith.png"), new RenderPlayer(Minecraft.getMinecraft().getRenderManager())
        );
    }

    public static void init() {
        ZOMBIE = registerPoly();
        WRAITH = registerPoly();
    }

    public static int getPoly(EntityPlayer player) {
        try {
            return polymorphs.get(player.getUniqueID().toString());
        } catch (Exception e) {
            setPoly(player, 0);
            return 0;
        }
    }

    @SideOnly(Side.CLIENT)
    public static ModelBase getPolyModel(EntityPlayer player) {
        return polyModel.get(getPoly(player));
    }

    @SideOnly(Side.CLIENT)
    public static ResourceLocation getPolyTexture(EntityPlayer player) {
        return polyTexture.get(getPoly(player));
    }

    @SideOnly(Side.CLIENT)
    public static RenderLivingBase getPolyRenderer(EntityPlayer player) {
        return polyRenderer.get(getPoly(player));
    }

    public static void setPoly(EntityPlayer p, int i) {
        polymorphs.put(p.getUniqueID().toString(), i);
    }

    public static void setPoly(String s, int i) {
        polymorphs.put(s, i);
    }

    public static void clear() {
        polymorphs.clear();
    }
}
