package com.machinespray.ROYAL.sync;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.items.rings.RingAction;
import com.machinespray.ROYAL.items.scrolls.ScrollAction;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;

public class KnowledgeRequestHandler implements
        IMessageHandler<MessageRequestKnowledge, IMessage> {

    public static void sendKnowledge(EntityPlayerMP player) {
        ArrayList<String> knowledge = Main.getHandler(player).getKnowledge();
        for (int i = 0; i < knowledge.size(); i++)
            Main.WRAPPER_INSTANCE.sendTo(new MessageSendKnowledge(knowledge.get(i)),
                    player);
    }

    @Override
    public IMessage onMessage(MessageRequestKnowledge message,
                              MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        sendKnowledge(player);
        for (int i = 0; i < RingAction.values().length; i++) {
            Main.WRAPPER_INSTANCE.sendTo(
                    new MessageSendKnowledge(RingAction.values()[i].getKnowledgeName(),
                            RingAction.values()[i].id, EnumDataType.RING),
                    player);
        }
        for (int i = 0; i < ScrollAction.values().length; i++) {
            Main.WRAPPER_INSTANCE.sendTo(
                    new MessageSendKnowledge(ScrollAction.values()[i].getKnowledgeName(),
                            ScrollAction.values()[i].id,
                            EnumDataType.SCROLL), player);
        }
        return null;
    }
}