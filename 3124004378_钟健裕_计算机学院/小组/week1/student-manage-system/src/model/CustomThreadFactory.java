package model;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

//自定义线程工厂
public class CustomThreadFactory implements ThreadFactory {
    private final AtomicInteger id=new AtomicInteger(1);
    //线程安全的计数器

    @Override
    public Thread newThread(Runnable r) {
        //创建线程
        Thread thread=new Thread(r);
        //设置线程名
        thread.setName("线程"+id.getAndIncrement()+"号");
        //返回线程
        return thread;
    }
}

