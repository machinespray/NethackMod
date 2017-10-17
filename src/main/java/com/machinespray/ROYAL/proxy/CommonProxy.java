package com.machinespray.ROYAL.proxy;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.items.NetHackItem;
import com.machinespray.ROYAL.knowledge.DefaultKnowledgeHandler;
import com.machinespray.ROYAL.knowledge.IKnowledgeHandler;
import com.machinespray.ROYAL.knowledge.Storage;
import com.machinespray.ROYAL.polymorph.PolyPlayerData;
import com.machinespray.ROYAL.sync.KnowledgeMessageHandler;
import com.machinespray.ROYAL.sync.KnowledgeRequestHandler;
import com.machinespray.ROYAL.sync.MessageRequestKnowledge;
import com.machinespray.ROYAL.sync.MessageSendKnowledge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy implements Constants {
	
	public void preinit(){
		//RoyalItems.initItems();
		//RoyalItems.registerItems();
		CapabilityManager.INSTANCE.register(IKnowledgeHandler.class, new Storage(), DefaultKnowledgeHandler.class);
	}
	public void init(){
		PolyPlayerData.init();
		Main.INSTANCE.registerMessage(KnowledgeMessageHandler.class, MessageSendKnowledge.class, 0, Side.CLIENT); 
		Main.INSTANCE.registerMessage(KnowledgeRequestHandler.class, MessageRequestKnowledge.class, 1, Side.SERVER);
	}
	public void postinit(){
	}
	public String getUse(NetHackItem item) {
		try{
		return item.actionGroup.getAction(item.getUnlocalizedName()).getKnowledgeName();
		}catch(Exception e){
			return null;
		}
	}

}
