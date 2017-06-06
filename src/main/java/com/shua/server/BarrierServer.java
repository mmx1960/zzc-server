package com.shua.server;

import com.shua.server.decoder.EndBasedDecoder;
import com.shua.server.encoder.TestEncoder;
import com.shua.server.entity.SocketModel;
import com.shua.server.handler.BarrierServerHandler;
import com.shua.server.util.Content;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Discards any incoming data.
 */
public class BarrierServer implements Runnable {

    public static Map map = new ConcurrentHashMap();
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(16);
    private int port;

    public BarrierServer() {
        this.port = 8888;
    }

    public BarrierServer(int port) {
        this.port = port;
    }
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
                     //添加结束符解码器
                     ch.pipeline().addLast("decoder1",new EndBasedDecoder(new SocketModel(),Content.END,Content.ACK,Content.REQ,Content.ANS,Content.NAK));
                     //添加编码器
                     ch.pipeline().addLast("encoder",new TestEncoder());
                     //添加帧解码器和业务处理
                     ch.pipeline().addLast(group, "handler", new BarrierServerHandler());
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)
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
    public void start(){
        new Thread(this).start();
    }
}