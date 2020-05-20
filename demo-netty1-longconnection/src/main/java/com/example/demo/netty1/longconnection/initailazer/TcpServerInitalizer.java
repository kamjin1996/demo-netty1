package com.example.demo.netty1.longconnection.initailazer;

import com.example.demo.netty1.longconnection.handler.TcpServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by jack on 2018/5/5.
 */
public class TcpServerInitalizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
        //添加客户端和服务端之间的心跳检查状态
        ch.pipeline().addLast(new IdleStateHandler(6, 2, 1, TimeUnit.SECONDS));
        ch.pipeline().addLast(new TcpServerHandler());
        ch.pipeline().addLast(new StringEncoder());
    }
}
