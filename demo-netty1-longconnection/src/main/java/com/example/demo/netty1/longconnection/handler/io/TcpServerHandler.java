package com.example.demo.netty1.longconnection.handler.io;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.netty1.longconnection.bean.Response;
import com.example.demo.netty1.longconnection.bean.ServerRequest;
import com.example.demo.netty1.longconnection.handler.business.TcpUserHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Created by jack on 2018/5/5.
 */
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(TcpServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf req = (ByteBuf) msg;
            String content = req.toString(Charset.defaultCharset());
            if (logger.isDebugEnabled()) {
                logger.debug("服务端开始读取客户端的请求数据:" + content);
            }

            //判断服务端和客户端是在能够正常通信的情况下
            // if (Objects.equals("ping", content)) {
            //     ctx.channel().writeAndFlush("ping\r\n");
            //     return;
            // }
            if (content.contains("张鑫鑫6")) {
                ChannelPipeline pipeline = ctx.pipeline();
                pipeline.addLast("user_handler", new TcpUserHandler());
            }
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


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (Objects.equals(event.state(), IdleState.READER_IDLE)) {
                System.out.println("读空闲====");
                ctx.close();
            } else if (Objects.equals(event.state(), IdleState.WRITER_IDLE)) {
                System.out.println("写空闲====");
            } else if (Objects.equals(event.state(), IdleState.ALL_IDLE)) {
                System.out.println("读写空闲====");
                ctx.channel().writeAndFlush("ping\r\n");
            }

        }

        super.userEventTriggered(ctx, evt);
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
