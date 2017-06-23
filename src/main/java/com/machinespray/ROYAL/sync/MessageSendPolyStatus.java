package com.machinespray.ROYAL.sync;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageSendPolyStatus implements IMessage {
    private int type;
    private String uuid;

    public MessageSendPolyStatus() {

    }

    public MessageSendPolyStatus(EntityPlayer player, int type) {
        uuid = player.getUniqueID().toString();
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = buf.readInt();
        uuid = "";
        int length = buf.readInt();
        for (int i = 0; i < length; i++)
            uuid = uuid + buf.readChar();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(type);
        buf.writeInt(uuid.length());
        for (char c : uuid.toCharArray())
            buf.writeChar(c);
    }
}
