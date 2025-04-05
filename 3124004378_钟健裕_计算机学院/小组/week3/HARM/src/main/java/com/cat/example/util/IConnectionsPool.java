package com.cat.example.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接池接口
 */
public interface IConnectionsPool {
    /**
     * 初始化连接池
     * @throws SQLException 数据库连接异常
     */
    public void init() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException;

    /**
     * 关闭连接池
     * 释放所有连接并关闭连接池
     */
    public void close() throws SQLException;

    /**
     * 获取数据库连接
     * @return 数据库连接
     */
    public Connection getConnection();

    /**
     * 释放数据库连接
     * @param conn 数据库连接
     */
    public void releaseConnection(Connection conn) throws InterruptedException, SQLException;
}
