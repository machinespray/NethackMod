package com.machinespray.ROYAL.proxy;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.blocks.RoyalBlocks;
import com.machinespray.ROYAL.items.RoyalItems;
import com.machinespray.ROYAL.sync.KnowledgeMessageHandler;
import com.machinespray.ROYAL.sync.KnowledgeRequestHandler;
import com.machinespray.ROYAL.sync.MessageRequestKnowledge;
import com.machinespray.ROYAL.sync.MessageSendKnowledge;
import com.machinespray.ROYAL.sync.knowledge.DefaultKnowledgeHandler;
import com.machinespray.ROYAL.sync.knowledge.IKnowledgeHandler;
import com.machinespray.ROYAL.sync.knowledge.Storage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy implements Constants {

    public void preinit() {
        RoyalItems.initItems();
        RoyalItems.registerItems();
        RoyalBlocks.initBlocks();
        RoyalBlocks.registerBlocks();
        CapabilityManager.INSTANCE.register(IKnowledgeHandler.class, new Storage(), DefaultKnowledgeHandler.class);
    }

    public void init() {
        Main.WRAPPER_INSTANCE.registerMessage(KnowledgeMessageHandler.class, MessageSendKnowledge.class, 0, Side.CLIENT);
        Main.WRAPPER_INSTANCE.registerMessage(KnowledgeRequestHandler.class, MessageRequestKnowledge.class, 1, Side.SERVER);
    }

    public void postinit() {
    }

    public boolean isRemote() {
        return false;
    }

}
