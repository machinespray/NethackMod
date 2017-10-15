package com.machinespray.ROYAL.polymorph;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.machinespray.ROYAL.items.NetHackItem;
import com.machinespray.ROYAL.polymorph.customEntities.MalleableHorse;
import com.machinespray.ROYAL.polymorph.customEntities.MalleableParrot;
import com.machinespray.ROYAL.polymorph.customEntities.MalleableWolf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
    public static int CAT;
    public static int DONKEY;
    public static int OWL;
    @SideOnly(Side.CLIENT)
    private static HashMap<Integer, ModelBase> polyModel = new HashMap<>();
    @SideOnly(Side.CLIENT)
    private static HashMap<Integer, ResourceLocation> polyTexture = new HashMap<>();
    @SideOnly(Side.CLIENT)
    private static HashMap<Integer, RenderLivingBase> polyRenderer = new HashMap<>();
    @SideOnly(Side.CLIENT)
    private static HashMap<Integer, EntityLivingBase> polyEntity = new HashMap<>();
    private static HashMap<Integer, Float> polySize = new HashMap<>();
    private static RenderPlayer defaultRender = new RenderPlayer(Minecraft.getMinecraft().getRenderManager());
    private static ModelPlayer defaultModel = new ModelPlayer(0.0F, false);
    private static int id = 1;


    public static int registerPoly() {
        polySize.put(id, 0F);
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
        polyRenderer.put(id,null);
        return registerPoly();
    }

    @SideOnly(Side.CLIENT)
    public static int registerPolyUnique(ModelBase model, ResourceLocation texture, RenderLivingBase renderer, float size, EntityLivingBase entity) {
        polyModel.put(id, model);
        polyTexture.put(id, texture);
        polyRenderer.put(id, renderer);
        polyEntity.put(id,entity);
        return registerPolyUnique(size);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        ZOMBIE = registerPoly(defaultModel, new
                ResourceLocation("royal", "textures/polymorph/zombie.png"), defaultRender);
        WRAITH = registerPoly(defaultModel, new
                ResourceLocation("royal", "textures/polymorph/wraith.png"), defaultRender);
        SILVERFISH = registerPolyUnique(new ModelSilverfish(), new ResourceLocation("textures/entity/silverfish.png"), new RenderSilverfish(renderManager), 0.6F,new EntitySilverfish(null));
        WOLF = registerPolyUnique(new ModelWolf(), new ResourceLocation("textures/entity/wolf/wolf.png"), new RenderWolf(renderManager), 0.8F, new MalleableWolf());
        CAT = registerPolyUnique(new ModelOcelot(), new ResourceLocation("textures/entity/cat/ocelot.png"), new RenderOcelot(renderManager), 0.6F,new EntityOcelot(null));
        DONKEY = registerPolyUnique(new ModelHorse(), new ResourceLocation("textures/entity/horse/donkey.png"), new RenderHorse(renderManager), 1.8F, new MalleableHorse());
    }

    public static void init() {
        ZOMBIE = registerPoly();
        WRAITH = registerPoly();
        SILVERFISH = registerPolyUnique(0.6F);
        WOLF = registerPolyUnique(0.8F);
        CAT = registerPolyUnique(0.6F);
        DONKEY = registerPolyUnique(1.8F);
        OWL = registerPolyUnique(.9F);
    }

    public static int getPoly(EntityPlayer player) {
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        int slots = handler.getSlots();
        for(int i=0; i<slots;i++)
            if(!handler.getStackInSlot(i).isEmpty()) {
                Item item = handler.getStackInSlot(i).getItem();
                if(item instanceof NetHackItem)
                    return (((NetHackItem) item).getID())%DONKEY+1;
            }
            return NONE;
    }

    @SideOnly(Side.CLIENT)
    public static EntityLivingBase getPolyEntity(EntityPlayer player) {
        return polyEntity.get(getPoly(player));
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
        try {
            return polySize.get(getPoly(player));
        }catch (Exception e){
            //This is being called early, because of a weird compat thing I did,return no polymorph for now, as it
            // doesn't matter yet
            return 0F;
        }
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
