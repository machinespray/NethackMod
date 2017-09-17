package com.machinespray.ROYAL;

import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import com.machinespray.ROYAL.polymorph.PolyEvents;
import com.machinespray.ROYAL.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.Random;

@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main {
    @CapabilityInject(IKnowledgeHandler.class)
    public static final Capability<IKnowledgeHandler> CAPABILITY_KNOWLEDGE = null;
    public static final String MODID = "royal";
    public static final String VERSION = "0.25";
    public static final Random random = new Random();
    @SidedProxy(modId = MODID, clientSide = "com.machinespray.ROYAL.proxy.ClientProxy", serverSide = "com.machinespray.ROYAL.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    public static String[] rings;
    public static String[] scrolls;

    public static final CreativeTabs royalTab = new CreativeTabs("royal") {

        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(RoyalItems.base);
        }

    }.setNoTitle();

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        proxy.preinit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        MinecraftForge.EVENT_BUS.register(new Events());
        MinecraftForge.EVENT_BUS.register(new PolyEvents());
    }

    @EventHandler
    public void postinit(FMLInitializationEvent event) {
        proxy.postinit();
    }

    public static IKnowledgeHandler getHandler(Entity entity) {
        if (entity.hasCapability(CAPABILITY_KNOWLEDGE, EnumFacing.DOWN))
            return entity.getCapability(CAPABILITY_KNOWLEDGE, EnumFacing.DOWN);
        return null;
    }

}
