package com.qg.zhongjianyu.dao;

import com.qg.zhongjianyu.dao.ORM.ResultSetToObject;
import com.qg.zhongjianyu.dao.ORM.SQLGenerator;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Data Access Object,数据库访问对象
 */


public class Dao {
    public static Logger log = Logger.getLogger(Dao.class.getName());

    private Connection conn;

    /**
     * 获取数据库连接
     */
    public void getConnection() {
        if (conn == null) {
            try {
                conn = StaticConnectionsPool.CONNECTIONS_POOL.getConnection();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 释放数据库连接
     */
    public void releaseConnection() {
        if (conn!= null) {
            try {
                StaticConnectionsPool.CONNECTIONS_POOL.releaseConnection(conn);
                conn = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行SQL更新语句
     * @param sql SQL语句
     * @param params 参数列表
     * @return 影响的行数
     */
    public int upDate(String sql, Object[] params){
        log.info("\n传入SQL更新语句：" + sql
                + "\n参数列表：" + params.toString());
        // 定义影响的行数数组
        int row = -1;
        if(sql == null){
            return row;
        }
        try{
            // 获取数据库连接
            getConnection();
            // 创建PreparedStatement对象，预编译SQL语句
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // 设置参数
            for(int i = 0; i < params.length; i++){
                pstmt.setObject(i+1, params[i]);
            }
            // 执行更新
            row = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 释放数据库连接
            releaseConnection();
        }
        return row;
    }

    /**
     * 执行SQL更新语句
     * @param sql SQL语句
     * @return 影响的行数
     */
    public int upDate(String sql) {
        log.info("\n传入SQL更新语句：" + sql);
        // 定义影响的行数数组
        int row = -1;
        if (sql == null) {
            return row;
        }
        try {
            // 获取数据库连接
            getConnection();
            // 创建PreparedStatement对象，预编译SQL语句
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // 执行更新
            row = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 释放数据库连接
            releaseConnection();
        }
        return row;
    }

    /**
     * 执行SQL查询语句
     * @param sql SQL语句
     * @return 结果集
     */
    public ResultSet query(String sql) {
        log.info("\n传入SQL查询语句：" + sql);
        ResultSet rs = null;
        if(sql == null){
            return rs;
        }
        try {
            // 获取数据库连接
            getConnection();
            // 创建PreparedStatement对象，预编译SQL语句
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // 执行查询
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseConnection();
        }
        return rs;
    }

    /**
     * 传入对象，执行SQL插入语句
     * @param obj 对象
     * @return 影响的行数
     */
    public int add(Object obj) throws IllegalAccessException {
        // 编写SQL语句
        String sql = SQLGenerator.generateInsertSQL(obj);
        // 执行SQL插入语句
        return upDate(sql);
    }

    /**
     * 传入对象，执行SQL更新语句
     * @param obj 对象
     * @return 影响的行数
     */
    public int change(Object obj) throws IllegalAccessException {
        // 编写SQL语句
        String sql = SQLGenerator.generateUpdateSQL(obj);
        // 执行SQL更新语句
        return upDate(sql);
    }

    /**
     * 传入对象，执行SQL查询语句
     * @param entityClass 对象类
     * @param where 条件语句
     * @return 结果集
     */
    public ResultSet select(Class<?> entityClass, String where) {
        // 编写SQL语句
        String sql = SQLGenerator.generateSelectSQL(entityClass, where);
        // 执行SQL查询语句
        return query(sql);
    }

    /**
     * 传入对象，执行SQL查询语句
     * @param entityClass 对象类
     * @param where 条件语句
     * @return 对象
     */
    public <T> T selectClass(Class<?> entityClass,String where) {
        //获取结果集
        ResultSet rs = select(entityClass, where);
        //将结果集转换为对象
        try {
            return (T) ResultSetToObject.resultSetToObject(rs, entityClass);
        } catch (SQLException | InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 批量查询
     * @param entityClass 对象类
     * @param where 条件语句
     */
    public <T> List<T> selectList(Class<T> entityClass, String where) {
        //获取结果集
        try (ResultSet rs = select(entityClass, where)) {
            //将结果集转换为对象列表
            List<T> list = new ArrayList<>();
            while (true) {
                //将结果集的每一行转换为对象
                T obj = (T) ResultSetToObject.resultSetToObject(rs, entityClass);
                if (obj == null) {
                    break;
                }
                list.add(obj);
            }
            return list;
        } catch (SQLException | InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取表的总行数
     * @param entityClass 对象类
     * @return 总行数
     */
    public int total(Class<?> entityClass){
        //获取表名
        String tableName = SQLGenerator.getTableName(entityClass);
        //编写SQL语句
        String sql = "SELECT COUNT(*) FROM " + tableName;
        //执行SQL查询语句
        ResultSet rs = query(sql);
        //获取结果集的第一行第一列
        int total = 0;
        try {
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * 获取表的满足条件的行的总行数
     * @param entityClass 对象类
     * @param where 条件语句
     * @return 总行数
     */
    public int total(Class<?> entityClass, String where){
        //获取表名
        String tableName = SQLGenerator.getTableName(entityClass);
        //编写SQL语句
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + where;
        //执行SQL查询语句
        ResultSet rs = query(sql);
        //获取结果集的第一行第一列
        int total = 0;
        try {
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * 获取表的符合条件的行的数
     * @param entityClass 对象类
     * @param where 条件语句
     * @return 符合条件的行的数
     */
    public int count(Class<?> entityClass, String where){
        //编写SQL语句
        if (where == null || where.isEmpty()){
            return total(entityClass);
        }
        //获取表名
        String tableName = SQLGenerator.getTableName(entityClass);
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + where;
        //执行SQL查询语句
        ResultSet rs = query(sql);
        //获取结果集的第一行第一列
        int count = 0;
        try {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 获取特定条件下特定范围的行
     * 可指定起始位置、数量
     * @param entityClass 对象类
     * @param start 起始位置
     * @param size 数量
     * @param where 条件语句
     */
    public <T> List<T> selectRange(Class<T> entityClass, int start, int size, String where) {
        if (start < 0 || size <= 0) {
            throw new IllegalArgumentException("start 或 size 不能小于0 ");
        }
        //获取表名
        String tableName = SQLGenerator.getTableName(entityClass);
        //编写SQL语句
        String sql;
        if (where == null || where.isEmpty()) {
            sql = "SELECT * FROM " + tableName + " LIMIT " + start + "," + size;
        } else {
            sql = "SELECT * FROM " + tableName + " WHERE " + where + " LIMIT " + start + "," + size;
        }
        //执行SQL查询语句
        try (ResultSet rs = query(sql)) {
            //将结果集转换为对象列表
            List<T> list = new ArrayList<>();
            while (true) {
                //将结果集的每一行转换为对象
                T obj = (T) ResultSetToObject.resultSetToObject(rs, entityClass);
                if (obj == null) {
                    break;
                }
                list.add(obj);
            }
            return list;
        } catch (SQLException | InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取表的符合条件的行的数
     * 可指定起始位置、数量，排序方法，排序字段
     * @param entityClass 对象类
     * @param where 条件语句
     * @param start 起始位置
     * @param size 数量
     * @param isAsc 是否升序排序
     * @param orderField 排序字段
     */
    public <T> List<T> selectRange(Class<T> entityClass, String where, int start, int size, boolean isAsc, String orderField) {
        if (start < 0 || size <= 0) {
            throw new IllegalArgumentException("start 或 size 不能小于0 ");
        }
        //获取表名
        String tableName = SQLGenerator.getTableName(entityClass);
        //编写SQL语句
        String sql;
        if (where == null || where.isEmpty()) {
            sql = "SELECT * FROM " + tableName + (((orderField==null)||orderField.isEmpty())?(" "):(" ORDER BY " + orderField + " ")) + (isAsc?"ASC":"DESC") + " LIMIT " + start + "," + size;
        } else {
            sql = "SELECT * FROM " + tableName + " WHERE " + where + (((orderField==null)||orderField.isEmpty())?(" "):(" ORDER BY " + orderField + " ")) + (isAsc?"ASC":"DESC") + " LIMIT " + start + "," + size;
        }
        //执行SQL查询语句
        try (ResultSet rs = query(sql)) {
            //将结果集转换为对象列表
            List<T> list = new ArrayList<>();
            while (true) {
                //将结果集的每一行转换为对象
                T obj = ResultSetToObject.resultSetToObject(rs, entityClass);
                if (obj == null) {
                    break;
                }
                list.add(obj);
            }
            return list;
        } catch (SQLException | InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


//    /**
//     * 执行SQL更新语句
//     * @param sql SQL语句
//     * @param params 参数列表
//     * @return 影响的行数
//     */
//    public int[] upDate(String sql, List<Object[]> params){
//        log.info("\n传入SQL更新语句：" + sql
//                + "\n参数列表：" + params.toString());
//        // 定义影响的行数数组
//        int[] rows = null;
//        if(sql == null){
//            return rows;
//        }
//        try{
//            // 获取数据库连接
//            getConnection();
//            // 创建PreparedStatement对象，预编译SQL语句
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            // 设置参数
//            for(int i = 0; i < params.size(); i++){
//                Object[] param = params.get(i);
//                for(int j = 0; j < param.length; j++){
//                    pstmt.setObject(j+1, param[j]);
//                }
//                rows[i]=pstmt.executeUpdate();
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }finally {
//            // 释放数据库连接
//            releaseConnection();
//        }
//        return rows;
//    }
//    /**
//     * 执行查询SQL语句
//     * @param sql SQL语句
//     * @return 结果集
//     */
//    public ResultSet executeQuery(String sql) {
//        log.info("\n传入SQL查询语句：" + sql);
//        ResultSet rs = null;
//        if(sql == null){
//            return rs;
//        }
//        try {
//            // 获取数据库连接
//            getConnection();
//            // 创建PreparedStatement对象，预编译SQL语句
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            // 执行查询
//            rs = pstmt.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            releaseConnection();
//        }
//        return rs;
//    }
//    /**
//     * 添加数据
//     * @param params 参数列表
//     */
//    public void add(String tableName,List<Object[]> params) {
//        //编写SQL语句
//        String sql = "INSERT INTO "+tableName;
//        // (column1, column2, column3) VALUES (?,?,?);
//
//        //执行SQL批量更新语句
//    }


}
