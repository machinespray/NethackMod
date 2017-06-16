package com.machinespray.ROYAL.proxy;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.RoyalItems;
import com.machinespray.ROYAL.altars.RoyalBlocks;
import com.machinespray.ROYAL.knowledge.DefaultKnowledgeHandler;
import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import com.machinespray.ROYAL.knowledge.Storage;
import com.machinespray.ROYAL.rings.ItemRing;
import com.machinespray.ROYAL.rings.RingAction;
import com.machinespray.ROYAL.scrolls.ItemScroll;
import com.machinespray.ROYAL.scrolls.ScrollAction;
import com.machinespray.ROYAL.sync.KnowledgeMessageHandler;
import com.machinespray.ROYAL.sync.KnowledgeRequestHandler;
import com.machinespray.ROYAL.sync.MessageRequestKnowledge;
import com.machinespray.ROYAL.sync.MessageSendKnowledge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy implements Constants {
	
	public void preinit(){
		RoyalItems.initItems();
		RoyalItems.registerItems();
		RoyalBlocks.initBlocks();
		RoyalBlocks.registerBlocks();
		CapabilityManager.INSTANCE.register(IKnowledgeHandler.class, new Storage(), DefaultKnowledgeHandler.class);
	}
	public void init(){
		Main.INSTANCE.registerMessage(KnowledgeMessageHandler.class, MessageSendKnowledge.class, 0, Side.CLIENT); 
		Main.INSTANCE.registerMessage(KnowledgeRequestHandler.class, MessageRequestKnowledge.class, 1, Side.SERVER);
	}
	public void postinit(){
	}
	public String getRingUse(ItemRing ring) {
		try{
		return RingAction.getAction(ring.getUnlocalizedName()).getKnowledgeName();
		}catch(Exception e){
			return null;
		}
	}
	public String getScrollUse(ItemScroll scroll) {
		try{
		return ScrollAction.getAction(scroll.getUnlocalizedName()).getKnowledgeName();
		}catch(Exception e){
			return null;
		}
	}

}
