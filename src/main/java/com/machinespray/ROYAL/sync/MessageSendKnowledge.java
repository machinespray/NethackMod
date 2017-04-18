package com.machinespray.ROYAL.sync;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageSendKnowledge implements IMessage {
	public ArrayList<String> knowledge;

	public MessageSendKnowledge(ArrayList<String> knowledge) {
		this.knowledge = knowledge;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int amount = buf.readInt();
		for(int i=0;i<amount;i++){
			int length = buf.readInt();
			char[] charList=new char[length];
			for(int j =0;j<length;j++)
				charList[j]=buf.readChar();
			String finalStr="";
			for(int k=0;k<length;k++)
				finalStr=finalStr+charList[k];
			knowledge.add(finalStr);
		}


	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(knowledge.size());
		for(int i=0;i<knowledge.size();i++){
			buf.writeInt(knowledge.get(i).length());
			for(int j=0;j<knowledge.get(i).length();j++)
			buf.writeChar(knowledge.get(i).toCharArray()[j]);
		}

	}
}