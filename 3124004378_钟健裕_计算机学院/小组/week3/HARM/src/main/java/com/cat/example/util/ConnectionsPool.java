package com.cat.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用于连接池的类
 * @author 钟健裕
 */
public class ConnectionsPool implements IConnectionsPool {
    // 锁
    private final Object lock = new Object();
    //数据库url
    private String url;
    // 数据库用户名
    private String username;
    //数据库密码
    private String password;
    //数据库驱动类
    private String driverClassName;
    //数据库连接池最大连接数
    private int maxActive;
    //数据库连接池最小连接数
    private int minIdle;
    //数据库连接池最大空闲时间
    private int maxIdleTime;
    //数据库连接池连接超时时间
    private int connectionTimeout;

    // 工作连接池,线程安全的hashmap，用于存放工作中的连接
    private ConcurrentHashMap<Connection, Instant> connectionWorkQueue;
    // 空闲连接池,线程安全的队列，用于存放空闲的连接
    private ArrayBlockingQueue<Connection> connectionFreeQueue;
    //用于记录连接的使用频率
    private int connectionFrequency;


    // 连接池状态
    private boolean isClosed;

    //定时清理空闲连接池的线程
    ScheduledExecutorService scheduler;

    /**
     * connectionFrequency记录连接的使用频率，用于定时清理空闲连接池
     */
    public synchronized void frequencyAdjust() {
        if (connectionFrequency< connectionWorkQueue.size()){
            connectionFrequency=connectionWorkQueue.size();
        }
    }

    /**
     * 初始化连接池
     */
    @Override
    public void init() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        //注册驱动
        driverClassName = "com.mysql.cj.jdbc.Driver";
        Class.forName(driverClassName).newInstance();
        //初始化连接池参数
        connectionFrequency=0;
        connectionWorkQueue = new ConcurrentHashMap<>(maxActive);
        connectionFreeQueue = new ArrayBlockingQueue<>(maxActive);
        //为空闲连接池添加minIdle个空闲连接
        for (int i = 0; i < minIdle; i++){
            Connection conn = DriverManager.getConnection(url, username, password);
            connectionFreeQueue.add(conn);
        }
        //启动定时清理空闲连接池的线程
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {cleanIdleConnection();}
        },

                300,
                300,
                TimeUnit.SECONDS);
    }

    /**
     * 获取一个数据库连接
     * @return 一个数据库连接
     */
    @Override
    public Connection getConnection() {
        if (isClosed){
            throw new RuntimeException("连接池已关闭！");
        }
        Connection conn = null;
        //尝试从空闲连接池中获取一个连接，不需要阻塞
        try {
            conn = connectionFreeQueue.poll();
            // 如果空闲连接池中没有可用连接，则尝试扩充连接池
            if (conn == null) {
                //检查workQueue是否已满
                if (connectionWorkQueue.size() >= maxActive) {
                    //如果workQueue已满，则在FreeQueue中等待
                    conn = connectionFreeQueue.poll(maxIdleTime, TimeUnit.MILLISECONDS);
                    //如果在等待时间内没有获取到连接，则抛出异常
                    if (conn == null) {
                        throw new RuntimeException("获取连接超时！");
                    }else {
                        //如果在等待时间内获取到连接，则将其加入到workQueue中
                        connectionWorkQueue.put(conn, Instant.now());
                        frequencyAdjust();
                    }
                } else {
                    //如果workQueue未满，则尝试创建新的连接
                    conn = DriverManager.getConnection(url, username, password);
                    //将新连接加入到workQueue中
                    connectionWorkQueue.put(conn, Instant.now());
                    frequencyAdjust();
                }
            }else {
                //如果空闲连接池中有可用连接，则将其加入到workQueue中
                connectionWorkQueue.put(conn, Instant.now());
                frequencyAdjust();
            }
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    /**
     * 释放一个数据库连接
     * @param conn 一个数据库连接
     */
    @Override
    public void releaseConnection(Connection conn) throws InterruptedException, SQLException {
        if (isClosed){
            conn.close();
            conn=null;
            return;
        }
        //从workQueue中移除连接
        connectionWorkQueue.remove(conn);
        //将连接加入到空闲连接池
        boolean success = connectionFreeQueue.offer(conn);
        //如果空闲连接池已满，则关闭连接
        if(!success){
            conn.close();
        }
        conn = null;
    }

    /**
     * 关闭连接池
     */
    @Override
    public void close() throws SQLException {
        if (isClosed){
            return;
        }
        //关闭空闲连接池中的所有连接
        for (Connection conn : connectionFreeQueue) {
            conn.close();
        }
        for (Connection conn : connectionWorkQueue.keySet()){
            conn.close();
        }
        //清空连接池
        connectionFreeQueue.clear();
        connectionWorkQueue.clear();
        isClosed = true;
    }

    //定时清理空闲连接池
    public void cleanIdleConnection() {
        //获取空闲连接池中空闲连接的数量
        int leaveFreeCount = minIdle - connectionWorkQueue.size();
        if(leaveFreeCount<0){
            leaveFreeCount=0;
        }
        int idleCount = connectionFrequency-connectionWorkQueue.size()-leaveFreeCount;
        //如果空闲连接池中有空闲连接，则清理
        if (idleCount > 0) {
            for (int i = 0; i < idleCount; i++) {
                try {
                    Connection conn = connectionFreeQueue.poll();
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        connectionFrequency=0;
    }

    //定时完成任务




    public ConnectionsPool() {
    }

    public ConnectionsPool(String url, String username, String password, String driverClassName, int maxActive, int minIdle, int maxIdleTime, int connectionTimeout) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClassName = driverClassName;
        this.maxActive = maxActive;
        this.minIdle = minIdle;
        this.maxIdleTime = maxIdleTime;
        this.connectionTimeout = connectionTimeout;
        // 初始化连接池
        try {
            init();
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return driverClassName
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * 设置
     * @param driverClassName
     */
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    /**
     * 获取
     * @return maxActive
     */
    public int getMaxActive() {
        return maxActive;
    }

    /**
     * 设置
     * @param maxActive
     */
    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    /**
     * 获取
     * @return minIdle
     */
    public int getMinIdle() {
        return minIdle;
    }

    /**
     * 设置
     * @param minIdle
     */
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    /**
     * 获取
     * @return maxIdleTime
     */
    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    /**
     * 设置
     * @param maxIdleTime
     */
    public void setMaxIdleTime(int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    /**
     * 获取
     * @return connectionTimeout
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * 设置
     * @param connectionTimeout
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String toString() {
        return "connectionsPool{url = " + url + ", username = " + username + ", password = " + password + ", driverClassName = " + driverClassName + ", maxActive = " + maxActive + ", minIdle = " + minIdle + ", maxIdleTime = " + maxIdleTime + ", connectionTimeout = " + connectionTimeout + "}";
    }
}
