package cn._94zichao.server.server;

import cn._94zichao.server.decoder.EndBasedDecoder;
import cn._94zichao.server.encoder.ToModelEncoder;
import cn._94zichao.server.handler.CommonServerHandler;
import cn._94zichao.server.util.Content;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zzc on 2017/6/17 0017.
 */
public class ZzcServer {
    private final Logger logger = LoggerFactory.getLogger(ZzcServer.class);

    private ChannelInitializer initializer;
    private Integer backLog = 128;
    private Boolean keepAlive = true;
    private Integer listenPort = 9999;
    private Integer bossGroupThread = 1;
    private Integer serverSelectorThreads = 1;
    private Boolean useEpoll = false;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private void init(){
        bossGroup = new NioEventLoopGroup(bossGroupThread, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("NettyBoss_%d", this.threadIndex.incrementAndGet()));
            }
        }); // (1)
        if (useEpoll()) {
            workerGroup = new EpollEventLoopGroup(serverSelectorThreads,
                    new ThreadFactory() {
                        private AtomicInteger threadIndex = new AtomicInteger(0);
                        private int threadTotal = serverSelectorThreads;

                        @Override
                        public Thread newThread(Runnable r) {
                            return new Thread(r, String.format("NettyServerEPOLLSelector_%d_%d", threadTotal,
                                    this.threadIndex.incrementAndGet()));
                        }
                    });
        } else {
            workerGroup = new NioEventLoopGroup(serverSelectorThreads,
                    new ThreadFactory() {
                        private AtomicInteger threadIndex = new AtomicInteger(0);
                        private int threadTotal = serverSelectorThreads;
                        @Override
                        public Thread newThread(Runnable r) {
                            return new Thread(r, String.format("NettyServerNIOSelector_%d_%d", threadTotal,
                                    this.threadIndex.incrementAndGet()));
                        }
                    });
        }
    }
    public void startServer(){
        init();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(useEpoll() ? EpollServerSocketChannel.class:NioServerSocketChannel.class) // (3)
                    .childHandler(initializer)
                    .option(ChannelOption.SO_BACKLOG, backLog)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, keepAlive); // (6)
            ChannelFuture f = b.bind(listenPort).sync(); // (7)
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("启动失败",e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    private boolean useEpoll() {
        return useEpoll
                && Epoll.isAvailable();
    }

    public static  class TcpChannelInitializer<T extends Channel> extends ChannelInitializer<T> {
        private Map<Object,Method[]> methodsMap;
        private boolean skipEnd = true;
        private byte    endFrame= Content.END;
        private long    readerIdleTime = 0;
        private long    writerIdleTime = 0;
        private TimeUnit idleType = TimeUnit.SECONDS;
        private int     serverWorkerThread = 5;
        private EventExecutorGroup executorGroup;

        private void init(){
            this.executorGroup = new DefaultEventExecutorGroup(serverWorkerThread,
                    new ThreadFactory() {

                        private AtomicInteger threadIndex = new AtomicInteger(0);

                        @Override
                        public Thread newThread(Runnable r) {
                            return new Thread(r, "NettyServerCodecThread_" + this.threadIndex.incrementAndGet());
                        }
                    });
        }
        @Override
        protected void initChannel(T ch) throws Exception {
            init();
            ch.pipeline().addLast(executorGroup,
                    new IdleStateHandler(readerIdleTime, writerIdleTime, 0, idleType),
                    new EndBasedDecoder(endFrame,skipEnd),
                    new ToModelEncoder(),
                    new CommonServerHandler(methodsMap));
        }

        public Map<Object, Method[]> getMethodsMap() {
            return methodsMap;
        }

        public void setMethodsMap(Map<Object, Method[]> methodsMap) {
            this.methodsMap = methodsMap;
        }

        public boolean isSkipEnd() {
            return skipEnd;
        }

        public void setSkipEnd(boolean skipEnd) {
            this.skipEnd = skipEnd;
        }

        public byte getEndFrame() {
            return endFrame;
        }

        public void setEndFrame(byte endFrame) {
            this.endFrame = endFrame;
        }

        public long getReaderIdleTime() {
            return readerIdleTime;
        }

        public void setReaderIdleTime(long readerIdleTime) {
            this.readerIdleTime = readerIdleTime;
        }

        public long getWriterIdleTime() {
            return writerIdleTime;
        }

        public void setWriterIdleTime(long writerIdleTime) {
            this.writerIdleTime = writerIdleTime;
        }

        public TimeUnit getIdleType() {
            return idleType;
        }

        public void setIdleType(TimeUnit idleType) {
            this.idleType = idleType;
        }
    }

    public ChannelInitializer getInitializer() {
        return initializer;
    }

    public ZzcServer setInitializer(ChannelInitializer initializer) {
        this.initializer = initializer;
        return this;
    }
    public  TcpChannelInitializer buildTcpInitializer(){
        return new TcpChannelInitializer();
    }

    public Integer getBackLog() {
        return backLog;
    }

    public void setBackLog(Integer backLog) {
        this.backLog = backLog;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Integer getListenPort() {
        return listenPort;
    }

    public void setListenPort(Integer listenPort) {
        this.listenPort = listenPort;
    }

    public Integer getBossGroupThread() {
        return bossGroupThread;
    }

    public void setBossGroupThread(Integer bossGroupThread) {
        this.bossGroupThread = bossGroupThread;
    }

    public Integer getServerSelectorThreads() {
        return serverSelectorThreads;
    }

    public void setServerSelectorThreads(Integer serverSelectorThreads) {
        this.serverSelectorThreads = serverSelectorThreads;
    }

    public Boolean getUseEpoll() {
        return useEpoll;
    }

    public void setUseEpoll(Boolean useEpoll) {
        this.useEpoll = useEpoll;
    }
}
