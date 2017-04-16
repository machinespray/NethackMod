package com.machinespray.ROYAL;

import java.util.Random;

import com.machinespray.ROYAL.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main
{
    public static final String MODID = "royal";
    public static final String VERSION = "0.1";
    public static final Random random = new Random();
    @SidedProxy(modId=MODID,clientSide="com.machinespray.ROYAL.proxy.ClientProxy",serverSide="com.machinespray.ROYAL.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static final CreativeTabs royalTab = new CreativeTabs("royal"){

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(RoyalItems.base);
		}
    	
    }.setNoTitle();
    @EventHandler
    public void preinit(FMLPreInitializationEvent event){proxy.preinit();}
    @EventHandler
    public void init(FMLInitializationEvent event){proxy.init();MinecraftForge.EVENT_BUS.register(new Events());}
    @EventHandler
    public void postinit(FMLInitializationEvent event){proxy.postinit();}
}
