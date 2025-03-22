package DataBaseConnectPool;

import java.sql.Connection;

/**
 * 连接池接口
 */
public interface IConnectionPool {
    /**
     * 从连接池中获取一个连接对象
     * @return 一个连接
     */
    Connection getConnection();
    /**
     * 归还一个连接到连接池中
     * @param conn 一个连接
     */
    void releaseConnection(Connection conn);

}
