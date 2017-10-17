package com.machinespray.ROYAL.polymorph;

import com.machinespray.ROYAL.polymorph.customEntities.MalleableHorse;
import com.machinespray.ROYAL.polymorph.customEntities.MalleableWolf;
import com.machinespray.ROYAL.polymorph.players.IPlayerMorph;
import com.machinespray.ROYAL.polymorph.players.MorphCommon;
import com.machinespray.ROYAL.polymorph.players.PlayerMorphClient;
import com.machinespray.ROYAL.polymorph.players.PolyMorphClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

//TODO make a cleaner, more organized polymorph class/object type
public class PolyPlayerData {
    public static HashMap<String, Integer> polymorphs = new HashMap<>();
    public static IPlayerMorph ZOMBIE;
    public static IPlayerMorph WRAITH;
    public static IPlayerMorph SILVERFISH;
    public static IPlayerMorph WOLF;
    public static IPlayerMorph CAT;
    public static IPlayerMorph DONKEY;

    public static void init() {
        ZOMBIE = new MorphCommon(true, 0.6F, 1.8F);
        WRAITH = new MorphCommon(true, 0.6F, 1.8F);
        SILVERFISH = new MorphCommon(false, 0.4F, 0.3F);
        WOLF = new MorphCommon(false, 0.6F, 0.85F);
        CAT = new MorphCommon(false, 0.6F, 0.7F);
        DONKEY = new MorphCommon(false, 0.6F, 0.7F);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        try {
            ZOMBIE = new PlayerMorphClient(ZOMBIE, new ResourceLocation("royal", "textures/polymorph/zombie.png"));
            WRAITH = new PlayerMorphClient(WRAITH, new ResourceLocation("royal", "textures/polymorph/wraith.png"));
            SILVERFISH = new PolyMorphClient(SILVERFISH, new ResourceLocation("textures/entity/silverfish.png"), new EntitySilverfish(null), new RenderSilverfish(renderManager), new ModelSilverfish());
            WOLF = new PolyMorphClient(WOLF, new ResourceLocation("textures/entity/wolf/wolf.png"), new MalleableWolf(), new RenderWolf(renderManager), new ModelWolf());
            CAT = new PolyMorphClient(CAT, new ResourceLocation("textures/entity/cat/ocelot.png"), new EntityOcelot(null), new RenderOcelot(renderManager), new ModelOcelot());
            DONKEY = new PolyMorphClient(DONKEY, new ResourceLocation("textures/entity/horse/donkey.png"), new MalleableHorse(), new RenderHorse(renderManager), new ModelHorse());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static IPlayerMorph getPoly(EntityPlayer player) {
        if (player.inventory.currentItem==1)
            return ZOMBIE;
        /*
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        int slots = handler.getSlots();
        for (int i = 0; i < slots; i++)
            if (!handler.getStackInSlot(i).isEmpty()) {
                Item item = handler.getStackInSlot(i).getItem();
                if (item instanceof NetHackItem)
                    return (((NetHackItem) item).getID()) % DONKEY + 1;
            }
        return NONE;
        */
        return null;
    }
}

