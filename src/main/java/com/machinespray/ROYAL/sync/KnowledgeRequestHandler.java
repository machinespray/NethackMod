package com.machinespray.ROYAL.sync;

import com.machinespray.ROYAL.EnumDataType;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.Values;
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
        Object[] actions = Values.ringInstance.getActions();
        for (int i = 0; i < actions.length; i++) {
            Main.WRAPPER_INSTANCE.sendTo(
                    new MessageSendKnowledge(actions[i].toString(),
                            Values.ringInstance.getIdFromAction(actions[i].toString()), EnumDataType.RING),
                    player);
        }
        //for (int i = 0; i < ScrollAction.values().length; i++) {
        //    Main.WRAPPER_INSTANCE.sendTo(
        //            new MessageSendKnowledge(ScrollAction.values()[i].getKnowledgeName(),
        //                    ScrollAction.values()[i].id,
        //                    EnumDataType.SCROLL), player);
        //}
        return null;
    }
}