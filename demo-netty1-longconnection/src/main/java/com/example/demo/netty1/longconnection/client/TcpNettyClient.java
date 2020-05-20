package com.example.demo.netty1.longconnection.client;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.netty1.longconnection.bean.ClientRequest;
import com.example.demo.netty1.longconnection.bean.DefaultFuture;
import com.example.demo.netty1.longconnection.bean.Response;
import com.example.demo.netty1.longconnection.bean.User;
import com.example.demo.netty1.longconnection.initailazer.TcpClientInitalizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Random;

/**
 * Created by jack on 2018/5/5.
 */
public class TcpNettyClient {
    public static ChannelFuture future = null;
    static EventLoopGroup group = null;
    static Bootstrap client = null;

    static {
        group = new NioEventLoopGroup();
        client = new Bootstrap();
        client.group(group);
        client.channel(NioSocketChannel.class);
        client.option(ChannelOption.SO_KEEPALIVE, true);
        client.handler(new TcpClientInitalizer());
        try {
            future = client.connect("localhost", 8891).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //发送数据的方法
    public static Object send(ClientRequest request) {
        try {
            System.out.println("客户端向服务端发送请求数据:" + JSONObject.toJSONString(request));
            //客户端直接发送请求数据到服务端
            future.channel().writeAndFlush(JSONObject.toJSONString(request));
            //根据\r\n进行换行
            future.channel().writeAndFlush("\r\n");
            //通过请求实例化请求和响应之间的关系
            DefaultFuture defaultFuture = new DefaultFuture(request);
            //通过请求ID，获取对应的响应处理结果
            Response response = defaultFuture.get(10);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(new UserRequestThread(i)).start();//模拟多线程并发请求
        }
    }

    /**
     * 模拟用户并发请求
     */
    static class UserRequestThread implements Runnable {
        private int requestId;

        public UserRequestThread(int requestId) {
            this.requestId = requestId;
        }

        @Override
        public void run() {
            synchronized (UserRequestThread.class) {
                ClientRequest request = new ClientRequest();
                request.setCommand("saveUser");
                User user = new User();
                user.setAge(new Random().nextInt(4) * requestId);
                user.setId(requestId);
                user.setName("jiahp" + requestId);
                request.setContent(user);
                //拿到请求的结果
                Object result = TcpNettyClient.send(request);
                System.out.println("客户端长连接测试返回结果:" + JSONObject.toJSONString(result));
                System.out.println("        ");
            }
        }
    }

}
