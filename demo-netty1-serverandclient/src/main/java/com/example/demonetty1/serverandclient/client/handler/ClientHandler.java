package com.example.demonetty1.serverandclient.client.handler;

import com.example.demonetty1.serverandclient.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author kam
 *
 * <p>
 * 客户端处理类
 * </p>
 */
public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    //处理服务端返回的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        System.out.println("接受到server响应数据: " + response.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}