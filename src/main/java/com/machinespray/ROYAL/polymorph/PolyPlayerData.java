package com.machinespray.ROYAL.polymorph;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RenderSilverfish;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

public class PolyPlayerData {
    public static HashMap<String, Integer> polymorphs = new HashMap<>();
    public static int NONE = 0;
    public static int ZOMBIE;
    public static int WRAITH;
    public static int SILVERFISH;
    public static int WOLF;
    @SideOnly(Side.CLIENT)
    private static HashMap<Integer, ModelBase> polyModel = new HashMap<>();
    @SideOnly(Side.CLIENT)
    private static HashMap<Integer, ResourceLocation> polyTexture = new HashMap<>();
    @SideOnly(Side.CLIENT)
    private static HashMap<Integer, RenderLivingBase> polyRenderer = new HashMap<>();
    private static HashMap<Integer, Float> polySize = new HashMap<>();
    private static RenderPlayer defaultRender = new RenderPlayer(Minecraft.getMinecraft().getRenderManager());
    private static ModelPlayer defaultModel = new ModelPlayer(0.0F, false);
    private static int id = 1;

    public static int registerPoly() {
        return id++;
    }

    public static int registerPolyUnique(float size) {
        polySize.put(id, size);
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
    public static int registerPolyUnique(ModelBase model, ResourceLocation texture, RenderLivingBase renderer, float size) {
        polyModel.put(id, model);
        polyTexture.put(id, texture);
        polyRenderer.put(id, renderer);
        return registerPolyUnique(size);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        ZOMBIE = registerPoly(defaultModel, new
                ResourceLocation("royal", "textures/polymorph/zombie.png"), defaultRender);
        WRAITH = registerPoly(defaultModel, new
                ResourceLocation("royal", "textures/polymorph/wraith.png"), defaultRender);
        SILVERFISH = registerPolyUnique(new ModelSilverfish(), new ResourceLocation("textures/entity/silverfish.png"), new RenderSilverfish(Minecraft.getMinecraft().getRenderManager()), 0.6F);
        ModelBase temp =new ModelWolf();
        temp.isChild=true;
        WOLF = registerPolyUnique(temp, new ResourceLocation("textures/entity/wolf/wolf.png"), new RenderWolf(Minecraft.getMinecraft().getRenderManager()), 0.8F);
    }

    public static void init() {
        ZOMBIE = registerPoly();
        WRAITH = registerPoly();
        SILVERFISH = registerPolyUnique(0.6F);
        WOLF = registerPolyUnique(0.6F);
    }

    public static int getPoly(EntityPlayer player) {
        return WOLF;
        /*
        try {
            return polymorphs.get(player.getUniqueID().toString());
        } catch (Exception e) {
            setPoly(player, 0);
            return 0;
        }*/
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

    public static float getPolySize(EntityPlayer player) {
        return polySize.get(getPoly(player));
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
