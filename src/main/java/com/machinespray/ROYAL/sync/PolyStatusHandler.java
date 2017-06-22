package com.machinespray.ROYAL.sync;

import com.machinespray.ROYAL.polymorph.PolyPlayerData;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PolyStatusHandler implements
        IMessageHandler<MessageSendPolyStatus, IMessage> {

    @Override
    public IMessage onMessage(MessageSendPolyStatus message, MessageContext ctx) {
        PolyPlayerData.setPoly(message.getUuid(), message.getType());
        return null;
    }
}