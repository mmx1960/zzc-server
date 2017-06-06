package cn._94zichao.server.decoder;

import cn._94zichao.server.util.ByteUtil;
import cn._94zichao.server.entity.SocketModel;
import cn._94zichao.server.util.Content;
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
    private byte[] start;
    private SocketModel sk;

    public EndBasedDecoder(byte end){
        this.end = end;
    }
    public EndBasedDecoder(SocketModel sk,byte end,byte... start){
        this.end = end;
        this.start = start;
        this.sk = sk;
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in,end);
        if (decoded != null) {
            sk = sk.getModel();
            int beforeHead=0;
            ByteBuf bf = (ByteBuf) decoded;
            for (int i =0;i<start.length;i++){
                if ((beforeHead = bf.bytesBefore(start[i])) >= 0){
                    sk.setHead(start[i]);
                }
            }
            if (sk.getHead() != 0) {
                //跳过头前面的字节
                bf.skipBytes(beforeHead);
                //获取全部字节
                byte[] temp = ByteUtil.readAllBytes(bf);
                if (temp==null||temp.length<4){
                    sk.setEnd(Content.END);
                    out.add(sk);
                    return;
                }
                //保存操作码
                sk.setType(temp[0]);
                //保存数据
                sk.setData(ByteUtil.getBytes(temp, 0, temp.length - 2));
                //保存CRC
                sk.setCrcData(ByteUtil.getBytes(temp, temp.length - 2, 2));
                //返回结果
                out.add(sk);
            }
        }
    }

    private Object decode(ChannelHandlerContext ctx, ByteBuf in,byte end) {
        //如果找到end，就把数据传给下个handler
       int i = in.bytesBefore(end);
       if(i > 0){
           final int length = i + 1;
           return in.readRetainedSlice(length);
       }
        return null;
    }
}
