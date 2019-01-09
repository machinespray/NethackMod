package com.machinespray.royal.sync;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageSendKnowledge implements IMessage {
	public int knowledge;
	public int id = -1;
	public int type;
	public String stknowledge = "";

	public MessageSendKnowledge() {
	}

	public MessageSendKnowledge(String knowledge) {
		this.stknowledge = knowledge;
	}

	public MessageSendKnowledge(int knowledge, int id, EnumDataType type) {
		this.knowledge = knowledge;
		this.id = id;
		this.type = type.id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.knowledge = buf.readInt();
		this.id = buf.readInt();
		this.type = buf.readInt();
		int temp = buf.readInt();
		for (int i = 0; i < temp; i++)
			stknowledge += buf.readChar();

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(knowledge);
		buf.writeInt(id);
		buf.writeInt(type);
		buf.writeInt(stknowledge.length());
		for (char c : stknowledge.toCharArray())
			buf.writeChar(c);
	}

}