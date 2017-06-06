package com.shua.server.tcpServer;

import com.shua.server.decoder.EndBasedDecoder;
import com.shua.server.encoder.ToModelEncoder;
import com.shua.server.handler.BarrierServerHandler;
import com.shua.server.util.Content;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by SHUA on 2017/5/25.
 */
public class BaseServer implements Runnable {

    public static Map map = new ConcurrentHashMap();
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(16);
    private int port;
    //编码器
    private List<ChannelInboundHandlerAdapter> decoders;
    //解码器
    private List<ChannelOutboundHandlerAdapter> encoders;
    //入站业务处理器
    private List<ChannelInboundHandlerAdapter> inHandler;
    //出站业务处理器
    private List<ChannelOutboundHandlerAdapter> outHandler;


    public BaseServer() {
        decoders = new ArrayList<>();
        encoders = new ArrayList<>();
        inHandler = new ArrayList<>();
        outHandler = new ArrayList<>();
        this.port = 8888;
    }


    /**
     * 创建一个新类
     * @return
     */
    public static BaseServer create() {
        return new BaseServer();
    }
    /**
     *设置端口
     * @param port
     * @return
     */
    public BaseServer port(int port) {
        this.port = port;
        return this;
    }


    /**
     *解码器
     * @param list
     * @return
     */
    public BaseServer decoder(ChannelInboundHandlerAdapter... list) {
        for (int i = 0 ;i< list.length;i++) {
            decoders.add(list[i]);
        }
        return this;
    }

    /**
     *编码器
     * @param list
     * @return
     */
    public BaseServer encoder(ChannelOutboundHandlerAdapter... list) {
        for (int i = 0 ;i< list.length;i++) {
            encoders.add(list[i]);
        }
        return this;
    }
    /**入站处理类
     *
     * @param list
     * @return
     */
    public BaseServer in(ChannelInboundHandlerAdapter... list) {
        for (int i = 0 ;i< list.length;i++) {
            inHandler.add(list[i]);
        }
        return this;
    }

    /**
     *出站处理类
     * @param list
     * @return
     */
    public BaseServer out(ChannelOutboundHandlerAdapter... list) {
        for (int i = 0 ;i< list.length;i++) {
            outHandler.add(list[i]);
        }
        return this;
    }


    /**
     * 主方法
     */
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
                            //添加解码器
                            if (decoders.size()>0){
                                for (ChannelInboundHandlerAdapter cha:decoders){
                                    ch.pipeline().addLast(cha.getClass().getName(),cha);
                                }
                            }
                            //添加编码器
                            if (encoders.size() > 0) {
                                for (ChannelOutboundHandlerAdapter cha:encoders){
                                    ch.pipeline().addLast(cha.getClass().getName(),cha);
                                }
                            }
                            //添加业务处理器
                            if (inHandler.size()>0){
                                for (ChannelInboundHandlerAdapter cha:inHandler){
                                    ch.pipeline().addLast(group,cha.getClass().getName(),cha);
                                }
                            }
                            //添加业务处理器
                            if (outHandler.size()>0){
                                for (ChannelOutboundHandlerAdapter cha:outHandler){
                                    ch.pipeline().addLast(group,cha.getClass().getName(),cha);
                                }
                            }
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

    public static void main(String[] args) {
        BaseServer.create().port(9999).decoder(new EndBasedDecoder(Content.END)).encoder(new ToModelEncoder()).in(new BarrierServerHandler()).start();
    }

}
