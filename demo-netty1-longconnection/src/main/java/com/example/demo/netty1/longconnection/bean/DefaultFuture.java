package com.example.demo.netty1.longconnection.bean;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author kam
 * <p>
 * 管理请求和响应的关系
 * 主要是通过唯一的请求标识id
 */
public class DefaultFuture {

    /**
     * 存储响应结果和自身绑定在一起
     */
    public final static Map<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<>();

    static {
        //设置为后台线程
        FutureTimeOutThread timeOutThread = new FutureTimeOutThread();
        timeOutThread.setDaemon(true);
        timeOutThread.start();
    }

    private final long start = System.currentTimeMillis();
    //请求id
    private long id;

    //请求id对应的响应结果
    private volatile Response response;
    //超时时间
    private long timeout;
    //获取锁
    private volatile Lock lock = new ReentrantLock();
    //线程通知条件
    private volatile Condition condition = lock.newCondition();

    public DefaultFuture(ClientRequest request) {
        // //获取对应的请求ID
        id = request.getId();
        // //存储当前的请求ID对应的上下文信息
        FUTURES.put(id, this);
    }

    /**
     * 存储服务器端的响应
     *
     * @param res
     */
    public static void recive(Response res) {

        //========================================
        //xxx 延迟发送
        if (Objects.equals(res.getId(), 3L)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //=========================================

        //找到res相对应的DefaultFuture
        DefaultFuture future = FUTURES.remove(res.getId());
        if (future == null) {
            return;
        }
        Lock lock = future.getLock();
        lock.lock();
        try {
            //设置响应
            future.setResponse(res);
            Condition condition = future.getCondition();
            if (condition != null) {
                //通知
                condition.signal();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 根据请求id获取响应结果
     *
     * @param timeout
     * @return
     */
    public Response get(long timeout) {
        long start = System.currentTimeMillis();
        lock.lock();//先锁
        while (!hasDone()) {
            try {
                condition.await(timeout, TimeUnit.SECONDS);
                if (System.currentTimeMillis() - start >= timeout) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();//释放锁
            }
        }
        return response;
    }

    private boolean hasDone() {
        return response != null;
    }

    public long getId() {
        return id;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Lock getLock() {
        return lock;
    }

    public Condition getCondition() {
        return condition;
    }

    public long getTimeout() {
        return timeout;
    }

    public long getStart() {
        return start;
    }

    /**
     * 处理请求超时的线程
     */
    static class FutureTimeOutThread extends Thread {
        @Override
        public void run() {
            while (true) {
                for (Long futureId : FUTURES.keySet()) {
                    DefaultFuture f = FUTURES.get(futureId);
                    if (f == null) {
                        //为空的话 代表请求结果已经处理完毕了
                        FUTURES.remove(futureId);
                        continue;
                    }
                    if (f.getTimeout() > 0) {
                        if ((System.currentTimeMillis() - f.getStart()) > f.getTimeout()) {
                            Response res = new Response();
                            res.setContent(null);
                            res.setMsg("请求超时！");
                            //响应异常处理
                            res.setStatus(-1);
                            res.setId(f.getId());

                            //存储服务端的响应结果信息
                            DefaultFuture.recive(res);
                        }
                    }
                }
            }
        }
    }

}
