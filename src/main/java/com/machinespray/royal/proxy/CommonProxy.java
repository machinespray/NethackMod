package com.machinespray.royal.proxy;

import com.machinespray.royal.Constants;
import com.machinespray.royal.Main;
import com.machinespray.royal.action.rings.ItemRing;
import com.machinespray.royal.action.rings.RingAction;
import com.machinespray.royal.action.scrolls.ItemScroll;
import com.machinespray.royal.action.scrolls.ScrollAction;
import com.machinespray.royal.knowledge.DefaultKnowledgeHandler;
import com.machinespray.royal.knowledge.IKnowledgeHandler;
import com.machinespray.royal.knowledge.Storage;
import com.machinespray.royal.sync.KnowledgeMessageHandler;
import com.machinespray.royal.sync.KnowledgeRequestHandler;
import com.machinespray.royal.sync.MessageRequestKnowledge;
import com.machinespray.royal.sync.MessageSendKnowledge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy implements Constants {

	public void preinit() {
		CapabilityManager.INSTANCE.register(IKnowledgeHandler.class, new Storage(), DefaultKnowledgeHandler::new);
	}

	public void init() {
		Main.INSTANCE.registerMessage(KnowledgeMessageHandler.class, MessageSendKnowledge.class, 0, Side.CLIENT);
		Main.INSTANCE.registerMessage(KnowledgeRequestHandler.class, MessageRequestKnowledge.class, 1, Side.SERVER);
	}

	public void postinit() {
	}

	public EntityPlayer getPlayer() {
		return null;
	}

	public String getRingUse(ItemRing ring) {
		try {
			return RingAction.getAction(ring.getUnlocalizedName()).getKnowledgeName();
		} catch (Exception e) {
			return null;
		}
	}

	public String getScrollUse(ItemScroll scroll) {
		try {
			return ScrollAction.getAction(scroll.getUnlocalizedName()).getKnowledgeName();
		} catch (Exception e) {
			return null;
		}
	}

}
