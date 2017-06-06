package cn._94zichao.server.handler;


import cn._94zichao.server.util.ByteUtil;
import cn._94zichao.server.entity.SocketModel;
import cn._94zichao.server.util.Content;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * Created by zzc on 2017/5/16.
 *根据类型调用不同的业务方法，并发送返回包
 */
public class BarrierServerHandler extends ChannelInboundHandlerAdapter { // (1)

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        SocketModel sk = (SocketModel)msg;
        //记录日志
        ctx.writeAndFlush(sk);
        //校验CRC
        if (!ByteUtil.isCRC(sk.retAllData(), sk.getCrcData())){
            //发送错误标识的包
            //返回
        }
        //通信请求
        if (sk.getHead()==Content.REQ) {
            if (sk.getType()==Content.UP_LOGIN_U){
                //处理设备注册
            }else if(sk.getType()==Content.UP_CNT_U){
                //处理上报人数
            }
        }//应答(不回复数据)
        else if (sk.getHead()==Content.ACK) {
            //解析状态码，更新系统状态
        }//回复数据
        else if (sk.getHead()==Content.ANS) {
            if(sk.getType()==Content.DAT_CNT_R){
                //处理上报人数
            }else if(sk.getType()==Content.DAT_VER_R){
                //处理程序版本
            }
        }//接收到不合法的命令
        else if (sk.getHead()==Content.NAK) {
            //解析状态码，更新系统状态
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //channel失效处理,客户端下线或者强制退出等任何情况都触发这个方法
        System.out.println("捕获异常");
        //移除当前channel句柄

        super.channelInactive(ctx);
    }
}