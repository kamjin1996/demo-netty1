package com.example.demo.netty1.filebrowser.http;

import com.example.demo.netty1.filebrowser.http.handler.HttpFileServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * @author kam
 *
 * <p>
 *
 * </p>
 */
public class HttpFileServer {
    //浏览文件的根目录
    private static final String DEFAULT_URL = "/";

    public void run(final int port, final String url) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 请求消息解码器
                    ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                    // 目的是将多个消息转换为单一的request或者response对象
                    ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                    // 响应解码器
                    ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                    // 目的是支持异步大文件传输（）
                    ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    // 业务逻辑
                    ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
                }
            });
            ChannelFuture future = b.bind("127.0.0.1", port).sync();
            System.out.println("HTTP文件目录服务器启动，网址是 : " + "http://127.0.0.1:" + port + url);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

