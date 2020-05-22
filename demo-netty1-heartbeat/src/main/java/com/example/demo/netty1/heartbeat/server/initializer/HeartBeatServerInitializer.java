package com.example.demo.netty1.heartbeat.server.initializer;

import com.example.demo.netty1.heartbeat.server.handler.HeartBeatServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author kam
 *
 * <p>
 *
 * </p>
 */
public class HeartBeatServerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
        //处理空闲状态事件的处理器
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringDecoder());
        //对空闲检测进一步处理的Handler
        pipeline.addLast(new HeartBeatServerHandler());
    }
}
