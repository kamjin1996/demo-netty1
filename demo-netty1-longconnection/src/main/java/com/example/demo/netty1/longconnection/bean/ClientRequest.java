package com.example.demo.netty1.longconnection.bean;


import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jack on 2018/5/5.
 * 封装客户端的请求
 */
public class ClientRequest {
    //使用原子技术
    private static final AtomicLong AL = new AtomicLong(0);
    private final long id;
    //请求命令
    private String command = "test";
    //请求参数
    private Object content;

    public ClientRequest() {
        //请求唯一标识id 每次都会自增加1
        id = AL.incrementAndGet();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

}
