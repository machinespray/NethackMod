package com.machinespray.royal.sync;

import com.machinespray.royal.Main;
import com.machinespray.royal.action.rings.RingAction;
import com.machinespray.royal.action.scrolls.ScrollAction;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KnowledgeMessageHandler implements
		IMessageHandler<MessageSendKnowledge, IMessage> {
	@SideOnly(Side.CLIENT)
	@Override
	public IMessage onMessage(MessageSendKnowledge message, MessageContext ctx) {
		if (message.id == -1) {
			Main.getHandler(Minecraft.getMinecraft().player).addKnowledge(message.stknowledge);
		} else if (EnumDataType.get(message.type) == EnumDataType.RING) {
			RingAction.match(message);
		} else if (EnumDataType.get(message.type) == EnumDataType.SCROLL) {
			ScrollAction.match(message);
		}
		return null;
	}

}