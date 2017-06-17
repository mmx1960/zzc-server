package cn._94zichao.server.decoder;

import cn._94zichao.server.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by zzc on 2017/5/17.
 * 以结束符判断一帧是否传输结束的解码器
 *
 **/
public class EndBasedDecoder extends ByteToMessageDecoder {
    private byte end;
    private boolean skip;

    public EndBasedDecoder(byte end,boolean skipEnd){
        this.end = end;
        this.skip = skipEnd;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(in,end);
        if (decoded != null) {
            ByteBuf bf = (ByteBuf) decoded;
            //获取全部字节
            byte[] temp = ByteUtil.readAllBytes(bf);
            out.add(temp);
        }
    }
    private Object decode(ByteBuf in,byte end) {
        //如果找到end，就把数据传给下个handler
       int i = in.bytesBefore(end);
       if(i > 0){
           final ByteBuf frame;
           if (skip){
               final int length = i;
               frame = in.readRetainedSlice(length);
               in.skipBytes(1);
           }else {
               final int length = i + 1;
               frame =  in.readRetainedSlice(length);
           }
         return frame;
       }
        return null;
    }
}
