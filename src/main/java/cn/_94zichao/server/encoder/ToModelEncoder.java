package cn._94zichao.server.encoder;

import cn._94zichao.server.util.ByteUtil;
import cn._94zichao.server.util.Content;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

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

        for (int i = 0; i < msg.length; i++) {
            ByteUtil.writeByte(out,msg[i]);
        }
        out.writeByte(Content.END);
    }

}
