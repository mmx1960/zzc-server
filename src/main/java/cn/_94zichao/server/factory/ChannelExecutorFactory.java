package cn._94zichao.server.factory;

import cn._94zichao.server.entity.SocketModel;
import cn._94zichao.server.factory.executor.Executor;
import cn._94zichao.server.util.CacheUtil;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zzc on 2017/5/18.
 * 执行类工厂，生成不同socket的执行器
 */
public class ChannelExecutorFactory {

    public static Executor getExecutor(SocketModel sk){
        return new Executor(sk);
    }





}
