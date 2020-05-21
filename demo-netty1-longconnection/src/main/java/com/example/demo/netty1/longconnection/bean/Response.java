package com.example.demo.netty1.longconnection.bean;

/**
 * Created by jack on 2018/5/5.
 * 封装响应
 */
public class Response {
    //请求ID
    private long id;

    //响应状态
    private int status;
    //响应内容
    private Object content;
    //请求返回信息
    private String msg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
