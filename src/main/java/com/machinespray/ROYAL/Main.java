package com.machinespray.ROYAL;

import com.machinespray.ROYAL.items.RoyalItems;
import com.machinespray.ROYAL.proxy.CommonProxy;
import com.machinespray.ROYAL.sync.knowledge.IKnowledgeHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main {
    @CapabilityInject(IKnowledgeHandler.class)
    public static final Capability<IKnowledgeHandler> CAPABILITY_KNOWLEDGE = null;
    public static final String MODID = "royal";
    public static final String VERSION = "0.24";
    public static final SimpleNetworkWrapper WRAPPER_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    public static final CreativeTabs royalTab = new CreativeTabs("royal") {

        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(RoyalItems.base);
        }

    }.setNoTitle();
    @SidedProxy(modId = MODID, clientSide = "com.machinespray.ROYAL.proxy.ClientProxy", serverSide = "com.machinespray.ROYAL.proxy.CommonProxy")
    public static CommonProxy proxy;
    @Mod.Instance
    public static Main instance = new Main();

    public static IKnowledgeHandler getHandler(Entity entity) {
        if (entity.hasCapability(CAPABILITY_KNOWLEDGE, EnumFacing.DOWN))
            return entity.getCapability(CAPABILITY_KNOWLEDGE, EnumFacing.DOWN);
        return null;
    }

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        proxy.preinit();
        ConfigHandler.config = new Configuration(event.getSuggestedConfigurationFile());
        ConfigHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        MinecraftForge.EVENT_BUS.register(new Events());
    }

    @EventHandler
    public void postinit(FMLInitializationEvent event) {
        proxy.postinit();
    }
}
