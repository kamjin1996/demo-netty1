package com.example.demo.netty1.longconnection.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.netty1.longconnection.bean.DefaultFuture;
import com.example.demo.netty1.longconnection.bean.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class TcpClientHandler extends ChannelInboundHandlerAdapter {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(TcpClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断服务端和客户端是在能够正常通信的情况下
        // if (msg.toString().equals("ping")) {
        //     ctx.channel().writeAndFlush("ping\r\n");
        //     return;
        // }
        if (logger.isDebugEnabled()) {
            logger.debug("客户端获取到服务端响应数据:" + msg.toString());
        }

        String str = getJSONObject(msg.toString()).toString();
        //读取服务端的响应结果
        Response res = JSONObject.parseObject(str, Response.class);
        //存储响应结果
        DefaultFuture.recive(res);
    }

    private JSONObject getJSONObject(String str) {
        JSONObject json = JSONObject.parseObject(str);
        json.remove("content");
        json.put("msg", "保存用户信息成功");
        return json;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }

    }
}
