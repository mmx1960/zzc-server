package com.shua.server.factory;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by SHUA on 2017/5/18.
 * 执行类工厂，生成不同socket的执行器
 */
public class ChannelExecutorFactory {

    public static Executor getExecutor(ChannelHandlerContext ctx,Object sk){

        return new Executor(ctx,sk);
    }


    /**
     * 执行类，向channel写入信息
     */
    static class Executor {
        private ChannelHandlerContext ctx;
        Object sk ;
        Executor(ChannelHandlerContext ctx,Object sk){
            this.ctx = ctx;
            this.sk = sk;
        }
        public void exec(){
            if (ctx.executor().inEventLoop()) {
                ctx.writeAndFlush(sk);
            } else {
                ctx.executor().execute(new Runnable() {
                    public void run() {
                        ctx.pipeline().writeAndFlush(sk);
                    }
                });

            }
        }
    }


}
