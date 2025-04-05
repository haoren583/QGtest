package com.cat.example.dao;

import com.cat.example.util.ConnectionsPool;

import java.io.IOException;
import java.util.Properties;

/**
 * @author 钟健裕
 * 此项目公用的连接池，使用静态代码块初始化，避免多次创建连接池
 * 连接池的配置参数在ConnectionsPoolPram.properties中配置
 */
public class StaticConnectionsPool {
    public static final ConnectionsPool CONNECTIONS_POOL;
    static {
        Properties prop = new Properties();
        try {
            prop.load(ConnectionsPool.class.getResourceAsStream("/ConnectionsPoolPram.properties"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String url = prop.getProperty("jdbc.url");
        String username = prop.getProperty("jdbc.username");
        String password = prop.getProperty("jdbc.password");
        String driverClassName = prop.getProperty("jdbc.driverClassName");
        int maxActive = Integer.parseInt(prop.getProperty("jdbc.maxActive"));
        int minIdle = Integer.parseInt(prop.getProperty("jdbc.minIdle"));
        int maxIdleTime = Integer.parseInt(prop.getProperty("jdbc.maxIdleTime"));
        int maxWaitTime = Integer.parseInt(prop.getProperty("jdbc.connectionTimeout"));
        CONNECTIONS_POOL = new ConnectionsPool(url, username, password, driverClassName, maxActive, minIdle, maxIdleTime, maxWaitTime);
    }
}
