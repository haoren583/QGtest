package com.cat.example.util;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 全局线程池工具类
 * @author 钟健裕
 */
public class ThreadPoolUtil {
    // 从配置文件中读取线程池大小
    public static final   ThreadPoolExecutor EXECUTOR;
    // 线程池参数
    // 核心线程数
    public static final   int CORE_POOL_SIZE;
    // 最大线程数
    public static final   int MAX_POOL_SIZE;
    // 线程存活时间
    public static final int KEEP_ALIVE_TIME;
    // 阻塞队列容量
    public static final int BLOCKING_QUEUE_CAPACITY;

    /*
      初始化线程池参数
     */
    static {
        Properties prop = new Properties();
        try {
            prop.load(ThreadPoolUtil.class.getResourceAsStream("/ThreadPoolParam.properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        CORE_POOL_SIZE = Integer.parseInt(prop.getProperty("corePoolSize"));
        MAX_POOL_SIZE = Integer.parseInt(prop.getProperty("maxPoolSize"));
        KEEP_ALIVE_TIME = Integer.parseInt(prop.getProperty("keepAliveTime"));
        BLOCKING_QUEUE_CAPACITY = Integer.parseInt(prop.getProperty("blockingQueueCapacity"));
        EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(BLOCKING_QUEUE_CAPACITY));
    }
}
