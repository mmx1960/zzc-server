package cn._94zichao.server.factory;

import cn._94zichao.server.entity.SocketModel;
import cn._94zichao.server.util.CacheUtil;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by SHUA on 2017/5/18.
 * 执行类工厂，生成不同socket的执行器
 */
public class ChannelExecutorFactory {

    public static Executor getExecutor(SocketModel sk){

        return new Executor(sk);
    }


    /**
     * 执行类，向channel写入信息
     */
      static class Executor {
        private ChannelHandlerContext ctx;
        byte[] data;
        Executor(SocketModel sk){
            this.data = sk.getData();
            this.ctx = CacheUtil.getChannelCache(sk.getChannelId());
        }
        public void exec(){
            if (ctx.executor().inEventLoop()) {
                ctx.writeAndFlush(data);
            } else {
                ctx.executor().execute(new Runnable() {
                    public void run() {
                        ctx.pipeline().writeAndFlush(data);
                    }
                });

            }
            ctx = null;
        }
    }


}
