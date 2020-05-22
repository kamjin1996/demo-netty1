package com.example.demo.netty1.heartbeat.client;

import com.example.demo.netty1.heartbeat.client.initializer.HeartBeatClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

/**
 * @author kam
 *
 * <p>
 *
 * </p>
 */
public class HeartBeatClient {
    public static void main(String[] args) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new HeartBeatClientInitializer());

            Channel channel = bootstrap.connect("localhost", 8891).sync().channel();

            //标准输入
            Scanner scanner = new Scanner(System.in);

            //利用死循环，不断读取客户端在控制台上的输入内容
            for (; ; ) {
                String next = scanner.next();
                System.out.println("输入：" + next);
                channel.writeAndFlush(next + "\r\n");
            }

        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
