package com.example.demo.netty1.serverandclient.client;

import com.example.demo.netty1.serverandclient.client.handler.ClientHandler;
import com.example.demo.netty1.serverandclient.protocol.RpcDecoder;
import com.example.demo.netty1.serverandclient.protocol.RpcEncoder;
import com.example.demo.netty1.serverandclient.protocol.RpcRequest;
import com.example.demo.netty1.serverandclient.protocol.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

/**
 * @author kam
 *
 * <p>
 *
 * </p>
 */
public class NettyClient {

    private final String host;
    private final int port;
    private Channel channel;

    //连接服务端的端口号地址和端口号
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        final EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)  // 使用NioSocketChannel来作为连接用的channel类
                .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("正在连接中...");
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new RpcEncoder(RpcRequest.class)); //编码request
                        pipeline.addLast(new RpcDecoder(RpcResponse.class)); //解码response
                        pipeline.addLast(new ClientHandler()); //客户端处理类

                    }
                });
        //发起异步连接请求，绑定连接端口和host信息
        final ChannelFuture future = b.connect(host, port).sync();

        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture arg0) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("连接服务器成功");

                } else {
                    System.out.println("连接服务器失败");
                    future.cause().printStackTrace();
                    group.shutdownGracefully(); //关闭线程组
                }
            }
        });

        this.channel = future.channel();
    }

    public Channel getChannel() {
        return channel;
    }

     public static void main(String[] args) throws Exception {
        NettyClient nettyClient = new NettyClient("127.0.0.1",8891);
                nettyClient.start();
        Channel channel = nettyClient.getChannel();
        //消息体
        RpcRequest request = new RpcRequest();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String next = scanner.next();

            if (Objects.equals(next, "exit")) {
                break;
            }

            request.setId(UUID.randomUUID().toString());
            request.setData(next);
            //channel对象可重用，保存在内存中，可供其它地方发送消息
            channel.writeAndFlush(request);
        }
    }
}
