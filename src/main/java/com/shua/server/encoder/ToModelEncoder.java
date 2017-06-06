package com.shua.server.encoder;

import com.shua.server.entity.SocketModel;
import com.shua.server.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by zzc on 2017/5/16.
 *
 */
public class ToModelEncoder extends MessageToByteEncoder<SocketModel> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, SocketModel socketModel, ByteBuf byteBuf) throws Exception {

        byteBuf.writeByte(socketModel.getHead());
        byteBuf.writeByte(socketModel.getType());
        byte[] data = socketModel.getData();
        for (int i = 0;i<data.length;i++){
            ByteUtil.writeByte(byteBuf,data[i]);
        }
        byteBuf.writeByte(socketModel.getEnd());


    }
}
