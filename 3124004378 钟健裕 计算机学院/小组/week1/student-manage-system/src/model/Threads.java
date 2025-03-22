package model;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Threads {
    public static final Logger log = Logger.getLogger(Threads.class.getName());
    //日志记录器
    public static ThreadPoolExecutor threadPool;
    //线程池

    //线程池初始化
    public static void threadPoolInit() {
        threadPool = new ThreadPoolExecutor(10, 25, 10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                //线程池队列
                new CustomThreadFactory(),
                //线程工厂
                new ThreadPoolExecutor.AbortPolicy()
                //线程池拒绝策略
        );
    }
}