package com.shua.server.encoder;

import com.shua.server.entity.SocketModel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

/**
 * Created by zzc on 2017/5/16.
 *
 */
public class TestEncoder extends MessageToByteEncoder<SocketModel> {
    int i = 0,j =0;
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, SocketModel socketModel, ByteBuf byteBuf) throws Exception {

        if (socketModel.getEnd()==0){
            byteBuf.writeCharSequence("服务端主动"+i+++"\n", Charset.defaultCharset());
        }else {
            byteBuf.writeCharSequence("客户端回写"+j+++"\n", Charset.defaultCharset());
        }

    }
}
