package cn._94zichao.server.handler;


import cn._94zichao.server.entity.SocketModel;
import cn._94zichao.server.util.CacheUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * Created by zzc on 2017/5/16.
 *根据类型调用不同的业务方法，并发送返回包
 */

public class BarrierServerHandler extends ChannelInboundHandlerAdapter { // (1)
    public Map<Object,Method[]> methodsMap;

    public BarrierServerHandler(Map<Object, Method[]> methodsMap) {
        this.methodsMap = methodsMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //缓存channel
        CacheUtil.cacheChannel(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        SocketModel sk = new SocketModel();
        sk.setChannelId(ctx.channel().id().asShortText());
        sk.setData((byte[])msg);
        if (methodsMap.size() == 0){

        }
        for (Object serviceBean:methodsMap.keySet()){
            Method[] methods = methodsMap.get(serviceBean);
            for (Method method:methods){
                try {
                    Class[] paramClasses = method.getParameterTypes();
                    if (paramClasses.length == 1&&paramClasses[0] == SocketModel.class){
                        method.invoke(serviceBean,sk);
                    }
                } catch (Exception e){

                }
            }
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
        CacheUtil.removeChannel(ctx);
        super.channelInactive(ctx);
    }
}