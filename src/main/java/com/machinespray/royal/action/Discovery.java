package com.machinespray.royal.action;

import com.machinespray.royal.Main;
import com.machinespray.royal.knowledge.IKnowledgeHandler;
import com.machinespray.royal.sync.MessageSendKnowledge;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;

public interface Discovery {
	default void discover(EntityPlayerMP player, String knowledgeName, boolean audio) {
		IKnowledgeHandler knowledge = Main.getHandler(player);
		if (!knowledge.hasKnowledge(knowledgeName)) {
			if (audio)
				player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.5F, 1.0F);
			knowledge.addKnowledge(knowledgeName);
			Main.INSTANCE.sendTo(new MessageSendKnowledge(knowledgeName), player);
		}
	}
}
