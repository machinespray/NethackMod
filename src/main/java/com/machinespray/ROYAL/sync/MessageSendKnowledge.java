package com.machinespray.ROYAL.sync;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageSendKnowledge implements IMessage {
	public String knowledge;
	public int id=-1;
	public int type;

	public MessageSendKnowledge() {
	}
	
	public MessageSendKnowledge(String knowledge) {
		this.knowledge = knowledge;
	}
	public MessageSendKnowledge(String knowledge,int id,EnumDataType type) {
		this.knowledge = knowledge;
		this.id = id;
		this.type = type.id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int amount = buf.readInt();
		knowledge = "";
		for (int i = 0; i < amount; i++) 
			knowledge = knowledge + Character.toString(buf.readChar());
			this.id = buf.readInt();
			this.type = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(knowledge.length());
		for (int j = 0; j < knowledge.length(); j++)
			buf.writeChar(knowledge.toCharArray()[j]);
			buf.writeInt(id);
			buf.writeInt(type);
	}

}