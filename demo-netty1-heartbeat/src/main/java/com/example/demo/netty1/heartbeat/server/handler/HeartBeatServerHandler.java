package com.example.demo.netty1.heartbeat.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author kam
 *
 * <p>
 *
 * </p>
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端发来：" + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("读取完成");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端已断开");
    }

    //管道中上一个Handler触发的事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;

        String eventType = null;

        switch (event.state()) {
            case READER_IDLE:
                eventType = "读空闲";
                break;
            case WRITER_IDLE:
                eventType = "写空闲";
                break;
            case ALL_IDLE:
                eventType = "读写空闲";
                break;
            default:

        }

        System.out.println(ctx.channel().remoteAddress() + "超时事件：" + eventType);
    }


}
