package cn._94zichao.server.bootstrap;

import cn._94zichao.server.annotation.ZzcService;
import cn._94zichao.server.decoder.EndBasedDecoder;
import cn._94zichao.server.encoder.ToModelEncoder;
import cn._94zichao.server.handler.BarrierServerHandler;
import cn._94zichao.server.util.Content;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/6/17 0017.
 */
public class ZzcServer implements ApplicationContextAware,InitializingBean {
    public Map<Object,Method[]> methodsMap;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> map =applicationContext.getBeansWithAnnotation(ZzcService.class);
        for (Object serviceBean : map.values()) {
            try {
                //拿到类下面的所有方法
                Method[] methods = serviceBean.getClass().getDeclaredMethods();
                methodsMap = new HashMap();
                methodsMap.put(serviceBean, methods);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap b = new ServerBootstrap(); // (2)
                    b.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class) // (3)
                            .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                                @Override
                                public void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline().addLast("heartbeat",new IdleStateHandler(15, 0, 0, TimeUnit.SECONDS));
                                    ch.pipeline().addLast(new EndBasedDecoder(Content.END,true));

                                    //添加编码器
                                    ch.pipeline().addLast(new ToModelEncoder());

                                    //添加业务处理器
                                    ch.pipeline().addLast(new BarrierServerHandler(methodsMap));

                                }
                            })
                            .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                            .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
                    // Bind and start to accept incoming connections.
                    ChannelFuture f = b.bind(9999).sync(); // (7)
                    System.out.println(5);
                    // Wait until the server socket is closed.
                    // In this example, this does not happen, but you can do that to gracefully
                    // shut down your server.
                    f.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                }
            }
        }).start();

    }


}
