package com.machinespray.ROYAL.sync;

import com.machinespray.ROYAL.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KnowledgeMessageHandler implements
		IMessageHandler<MessageSendKnowledge, IMessage> {

	@Override
	public IMessage onMessage(MessageSendKnowledge message, MessageContext ctx) {
		Main.getHandler(Minecraft.getMinecraft().player).setKnowledge(message.knowledge);
		return null;
	}
}