package com.machinespray.ROYAL.sync;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.randomized.scroll.ScrollActionGroup;
import com.machinespray.ROYAL.rings.RingAction;
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
					new MessageSendKnowledge(RingAction.values()[i].getKnowledgeName(),
							RingAction.values()[i].id, 0),
					player);
		}
		for (int i = 0; i < ScrollActionGroup.INSTANCE.values().size(); i++) {
			Main.INSTANCE.sendTo(
					new MessageSendKnowledge(ScrollActionGroup.INSTANCE.values().get(i).getKnowledgeName(),
							ScrollActionGroup.INSTANCE.values().get(i).id,
							ScrollActionGroup.INSTANCE.id()), player);
		}
		return null;
	}
}