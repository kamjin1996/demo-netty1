package com.example.demo.netty1.longconnection.handler.business;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.netty1.longconnection.bean.Response;
import com.example.demo.netty1.longconnection.bean.ServerRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.nio.charset.Charset;

/**
 * @author kam
 *
 * <p>
 * Tcp用户处理器
 * </p>
 */
public class TcpUserHandler extends ChannelInboundHandlerAdapter {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(TcpUserHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        //处理用户业务
        Thread.sleep(5000);
        if (msg instanceof ByteBuf) {
            ByteBuf req = (ByteBuf) msg;
            String content = req.toString(Charset.defaultCharset());
            //判断服务端和客户端是在能够正常通信的情况下
            // if (Objects.equals("ping", content)) {
            //     ctx.channel().writeAndFlush("ping\r\n");
            //     return;
            // }
            //获取客户端的请求信息
            ServerRequest request = JSONObject.parseObject(content, ServerRequest.class);
            JSONObject user = (JSONObject) request.getContent();
            user.put("success", "ok");
            //写入解析请求之后结果对应的响应信息
            Response res = new Response();
            res.setId(request.getId());
            res.setContent(user);
            //先写入
            ctx.channel().write(JSONObject.toJSONString(res));
            //再一起刷新
            ctx.channel().writeAndFlush("\r\n");
            System.out.println("      ");
        }
    }
}
