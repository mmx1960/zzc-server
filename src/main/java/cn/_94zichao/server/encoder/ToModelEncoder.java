package cn._94zichao.server.encoder;

import cn._94zichao.server.util.ByteUtil;
import cn._94zichao.server.util.Content;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zzc on 2017/5/16.
 *
 */

public class ToModelEncoder extends MessageToByteEncoder<byte[]> {
    /**
     * Encode a message into a {@link ByteBuf}. This method will be called for each written message that can be handled
     * by this encoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToByteEncoder} belongs to
     * @param msg the message to encode
     * @param out the {@link ByteBuf} into which the encoded message will be written
     * @throws Exception is thrown if an error accour
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {

        byte[] all = new byte[256];
        int i = 1;
        int j = 1;
        all[0] = msg[0];
        while (true){
            if (i == msg.length-1){
                break;
            }
            byte cur =msg[i];
            byte[] bytes = ByteUtil.writeByte(cur);
            if (bytes[0] != cur){
                all[j++] = bytes[0];
                all[j++] = bytes[1];
                i++;
            }else {
                all[j++] = msg[i++];
            }
        }
        all[j++] = msg[i];
        out.writeBytes(ByteUtil.getBytes(all,0,j));
    }

}
