package cn._94zichao.server.util;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储系统中的缓存
 * Created by SHUA on 2017/6/8.
 */
public class CacheUtil {
    public static ConcurrentHashMap channelCache = new ConcurrentHashMap();

    /**
     * 保存当前连接的上下文
     *
     * @param ctx
     */
    public static void cacheChannel(ChannelHandlerContext ctx) {
        channelCache.putIfAbsent(ctx.channel().id().asShortText(), ctx);
    }

    /**
     * 移除当前连接的上下文
     *
     * @param ctx
     */
    public static void removeChannel(ChannelHandlerContext ctx) {
        channelCache.remove(ctx.channel().id().asShortText());
    }

    public static ChannelHandlerContext getChannelCache(String channelId) {

        return (ChannelHandlerContext) channelCache.get(channelId);

    }
}