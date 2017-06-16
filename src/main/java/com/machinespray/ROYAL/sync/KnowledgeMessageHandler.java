package com.machinespray.ROYAL.sync;

import com.machinespray.ROYAL.Main;
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
		if(message.id==-1){
		Main.getHandler(Minecraft.getMinecraft().player).addKnowledge(message.knowledge);
		}else if(EnumDataType.get(message.type)==EnumDataType.RING){
			Main.rings[message.id]=message.knowledge;
		}else if(EnumDataType.get(message.type)==EnumDataType.SCROLL){
			Main.scrolls[message.id]=message.knowledge;
		}
		return null;
	}
}