package com.machinespray.ROYAL.sync;

import java.util.ArrayList;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.rings.RingActions;
import com.machinespray.ROYAL.scrolls.ScrollActions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KnowledgeRequestHandler implements
		IMessageHandler<MessageRequestKnowledge, IMessage> {

	@Override
	public IMessage onMessage(MessageRequestKnowledge message,
			MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		ArrayList<String> knowledge = Main.getHandler(player).getKnowledge();
		for (int i = 0; i < knowledge.size(); i++)
			Main.INSTANCE.sendTo(new MessageSendKnowledge(knowledge.get(i)),
					player);
		RingActions.match(player.world.getSeed());
		for (int i = 0; i < RingActions.actions.size(); i++) {
			Main.INSTANCE.sendTo(
					new MessageSendKnowledge(RingActions.actions.get(i).name,
							RingActions.actions.get(i).id, EnumDataType.RING),
					player);
		}
		for (int i = 0; i < ScrollActions.actions.size(); i++) {
			Main.INSTANCE.sendTo(
					new MessageSendKnowledge(ScrollActions.actions.get(i).name,
							ScrollActions.actions.get(i).id,
							EnumDataType.SCROLL), player);
		}
		return null;
	}
}