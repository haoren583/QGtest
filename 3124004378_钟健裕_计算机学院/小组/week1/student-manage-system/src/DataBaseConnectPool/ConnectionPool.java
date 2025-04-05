package DataBaseConnectPool;

import org.junit.Test;

import java.sql.Connection;
import java.sql.*;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * 连接池的实现类
 * @author 钟健裕
 *
 * 此数据库连接池不具有归还连接相关的安全检查，不会对连接进行任何状态检查，
 * 因此，在使用连接池时，需要自觉维护连接的有效性，并即使归还连接到连接池。
 */
public class ConnectionPool implements IConnectionPool {

    public static final Logger log = Logger.getLogger(ConnectionPool.class.getName());
    // 数据库连接池的配置信息
    private String driverClass;
    private String url;
    private String username;
    private String password;
    // 最大活动连接数
    private int maxActive;
    // 空闲连接数
    private int freeConnections;
    // 最大等待时间
    private long maxWait;
    // 工作连接池,线程安全的hashmap，用于存放工作中的连接
    private ConcurrentHashMap<Connection, Instant> connectionWorkQueue;
    // 空闲连接池,线程安全的队列，用于存放空闲的连接
    private ArrayBlockingQueue<Connection> connectionFreeQueue;
    // 连接池状态
    private boolean isClosed;
//    //连接池已经建立的连接数计数器
//    private AtomicInteger buildedCount;
    public ConnectionPool() {
        init();
    }

    /**
     * 数据库连接池的初始化
     */
    public void init() {
        // 从配置文件中读取数据库连接池的配置信息
        driverClass = Preprocessor.getDriverClassName();
        //url = Preprocessor.getConnectionBatchUrl();
        url = Preprocessor.getUrl();
        username = Preprocessor.getUsername();
        password = Preprocessor.getPassword();
        maxActive = Preprocessor.getMaxActive();
        freeConnections = Preprocessor.getFreeConnections();
        maxWait = Preprocessor.getMaxWait();
        connectionFreeQueue = new ArrayBlockingQueue<Connection>(freeConnections);
        connectionWorkQueue=new ConcurrentHashMap<Connection,Instant>(maxActive);

        // 初始化连接池
        for (int i = 0; i < freeConnections; i++) {
            try {
                Connection conn = DriverManager.getConnection(url, username, password);
                connectionFreeQueue.offer(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // 连接池已经建立的连接数计数initialize
        //buildedCount=new AtomicInteger(initialSize);

        // 设置连接池状态为开启
        isClosed = false;

        // 输出连接池的初始化信息
        log.info("数据库连接池初始化成功！\n\t连接信息：" +
                "\n\t驱动类：" + driverClass +
                "\n\tURL：" + url +
                "\n\t用户名：" + username +
                //"\n\t初始连接数：" + initialSize +
                //"\n\t最大活动连接数：" + maxActive +
                "\n\t空闲连接数：" + freeConnections +
                "\n\t最大等待时间：" + maxWait +
                "\n\t连接池状态：开启");
        // 启动定时任务，定时关闭空闲连接
        //scheduleCloseIdleConnections();
    }

//    /**
//     * 关闭空闲连接
//     */
//    private void closeIdleConnections() {
//        // 计算应该清理的空闲连接数
//        int idleConnections = connectionQueue.size() - minIdle;
//        // 关闭空闲连接
//        if (idleConnections > 0) {
//            for (int i = 0; i < idleConnections; i++) {
//                try {
//                    Connection conn = connectionQueue.poll(maxWait, TimeUnit.MILLISECONDS);
//                    if (conn == null) {
//                        break;
//                    }
//                    // 关闭空闲连接
//                    conn.close();
//                } catch (InterruptedException | SQLException e) {
//                    // 通知线程停止正在执行的任务并进行相应的清理操作
//                    Thread.currentThread().interrupt();
//                    // 或者抛出自定义异常或错误信息
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    /**
//     * 扩充连接池
//     */
//    private void expandConnectionPool() {
//        //添加锁，防止buildedCount与实际连接数不一致
//        synchronized (this) {
//            // 如果已建立连接数小于最大活动连接数，则可以扩充连接池
//            // 尝试创建新的连接
//            if (buildedCount.get() < maxActive) {
//                try {
//                    // 创建新的连接
//                    Connection conn = DriverManager.getConnection(url, username, password);
//                    // 计数器加1
//                    buildedCount.incrementAndGet();
//                    // 将新连接加入到连接池中
//                    connectionQueue.offer(conn);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    /**
//     * 定时关闭空闲连接
//     */
//    private void scheduleCloseIdleConnections() {
//        // 创建一个ScheduledExecutorService，用于定时任务
//        ScheduledExecutorService closeScheduledExecutor = Executors.newScheduledThreadPool(1);
//        //添加一个定时任务，每隔60秒执行一次关闭空闲连接的任务
//        closeScheduledExecutor.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                closeIdleConnections();
//            }
//        }, 60, 60, TimeUnit.SECONDS);
//    }

    /**
     * 获取一个数据库连接
     * @return 一个数据库连接
     */
    @Override
    public Connection getConnection() {
        Connection conn = null;
        //尝试从空闲连接池中获取一个连接，不需要阻塞
        try {
            conn = connectionFreeQueue.poll();
            // 如果空闲连接池中没有可用连接，则尝试扩充连接池
            if (conn == null) {
                //检查workQueue是否已满
                if (connectionWorkQueue.size() >= maxActive) {
                    //如果workQueue已满，则在FreeQueue中等待
                    conn = connectionFreeQueue.poll(maxWait, TimeUnit.MILLISECONDS);
                    //如果在等待时间内没有获取到连接，则抛出异常
                    if (conn == null) {
                        throw new RuntimeException("获取连接超时！");
                    }else {
                        //如果在等待时间内获取到连接，则将其加入到workQueue中
                        connectionWorkQueue.put(conn, Instant.now());
                    }
                } else {
                    //如果workQueue未满，则尝试创建新的连接
                    conn = DriverManager.getConnection(url, username, password);
                    //将新连接加入到workQueue中
                    connectionWorkQueue.put(conn, Instant.now());
                }
            }else {
                //如果空闲连接池中有可用连接，则将其加入到workQueue中
                connectionWorkQueue.put(conn, Instant.now());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    /**
     * 释放一个数据库连接
     * @param conn 一个数据库连接
     */
    @Override
    public void releaseConnection(Connection conn) {
        //从workQueue中移除连接
        connectionWorkQueue.remove(conn);
        //将连接加入到空闲连接池
        boolean success = connectionFreeQueue.offer(conn);
        //如果空闲连接池已满，则尝试关闭一个连接
        if(!success){
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        conn = null;
    }

    //测试方法
    @Test
    public void test() {
//        ConnectionPool pool = new ConnectionPool();
//        Connection conn = pool.getConnection();
//        System.out.println(conn);
//        pool.releaseConnection(conn);
    }
}
