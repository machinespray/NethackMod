package com.machinespray.ROYAL.proxy;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.blocks.RoyalBlocks;
import com.machinespray.ROYAL.entity.EntityPotionRoyal;
import com.machinespray.ROYAL.entity.RenderPotionFactory;
import com.machinespray.ROYAL.items.RoyalItems;
import com.machinespray.ROYAL.items.rings.ItemRing;
import com.machinespray.ROYAL.items.scrolls.ItemScroll;
import com.machinespray.ROYAL.render.RenderGUIEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ClientProxy extends CommonProxy {
    static int id = 1;

    @Override
    public void preinit() {
        super.preinit();
        RoyalItems.registerClient();
        RoyalBlocks.registerClient();
        registerEntity(EntityPotionRoyal.class, new RenderPotionFactory());
    }

    private void registerEntity(Class EntityClass, IRenderFactory factory) {
        String name = EntityClass.getName().toLowerCase();
        EntityRegistry.registerModEntity(new ResourceLocation("royal", name), EntityClass, name, id++, Main.instance, 64, 3, true);
        RenderingRegistry.registerEntityRenderingHandler(EntityClass, factory);
    }

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(new RenderGUIEvent());
    }

    @Override
    public void postinit() {
        super.postinit();
    }

    @Override
    public String getRingUse(ItemRing ring) {
        if (ring.getID() != -2)
            return Main.rings[ring.getID()];
        return null;
    }

    @Override
    public String getScrollUse(ItemScroll scroll) {
        if (scroll.getID() != -2)
            return Main.scrolls[scroll.getID()];
        return null;
    }

    @Override
    public boolean isRemote() {
        return true;
    }

}
