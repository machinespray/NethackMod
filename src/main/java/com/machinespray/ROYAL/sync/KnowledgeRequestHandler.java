package com.machinespray.ROYAL.sync;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.action.rings.RingAction;
import com.machinespray.ROYAL.action.scrolls.ScrollAction;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;

public class KnowledgeRequestHandler implements
		IMessageHandler<MessageRequestKnowledge, IMessage> {

	@Override
	public IMessage onMessage(MessageRequestKnowledge message,
							  MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().player;
		ArrayList<String> knowledge = Main.getHandler(player).getKnowledge();
		for (int i = 0; i < knowledge.size(); i++)
			Main.INSTANCE.sendTo(new MessageSendKnowledge(knowledge.get(i)),
					player);
		RingAction.match(player.world.getSeed());
		for (int i = 0; i < RingAction.values().length; i++) {
			Main.INSTANCE.sendTo(
					new MessageSendKnowledge(i,
							RingAction.values()[i].id, EnumDataType.RING),
					player);
		}
		ScrollAction.match(player.world.getSeed());
		for (int i = 0; i < ScrollAction.values().length; i++) {
			Main.INSTANCE.sendTo(
					new MessageSendKnowledge(i,
							ScrollAction.values()[i].id,
							EnumDataType.SCROLL), player);
		}
		return null;
	}
}