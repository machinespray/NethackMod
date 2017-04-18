package com.machinespray.ROYAL.proxy;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.RoyalItems;
import com.machinespray.ROYAL.knowledge.DefaultKnowledgeHandler;
import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import com.machinespray.ROYAL.knowledge.Storage;
import com.machinespray.ROYAL.rings.RingActions;
import com.machinespray.ROYAL.scrolls.ScrollActions;
import com.machinespray.ROYAL.sync.KnowledgeMessageHandler;
import com.machinespray.ROYAL.sync.MessageSendKnowledge;

public class CommonProxy {
	
	public void preinit(){
		RoyalItems.initItems();
		RoyalItems.registerItems();
		CapabilityManager.INSTANCE.register(IKnowledgeHandler.class, new Storage(), DefaultKnowledgeHandler.class);
	}
	public  void init(){
		Main.INSTANCE.registerMessage(KnowledgeMessageHandler.class, MessageSendKnowledge.class, 0, Side.CLIENT); 
		RingActions.initActions();
		ScrollActions.initActions();
	}
	public void postinit(){
	}

}
