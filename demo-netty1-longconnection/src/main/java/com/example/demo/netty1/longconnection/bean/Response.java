package com.example.demo.netty1.longconnection.bean;

/**
 * Created by jack on 2018/5/5.
 * 封装响应
 */
public class Response {
    private long id;//请求ID
    private int status;//响应状态
    private Object content;//响应内容
    private String msg;//请求返回信息

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
