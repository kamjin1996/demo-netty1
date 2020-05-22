package com.example.demo.netty1.heartbeat.client.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author kam
 *
 * <p>
 *
 * </p>
 */
public class HeartBeatClientInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new StringDecoder());
    }
}
