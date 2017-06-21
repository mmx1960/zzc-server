package cn._94zichao.server.factory.executor;

import cn._94zichao.server.entity.SocketModel;
import cn._94zichao.server.util.CacheUtil;
import io.netty.channel.ChannelHandlerContext;

/**
     * 执行类，向channel写入信息
     */
     public   class Executor {
        private ChannelHandlerContext ctx;
        byte[] data;
        public Executor(SocketModel sk){
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
        }
    }