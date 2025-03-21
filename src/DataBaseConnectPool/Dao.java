package DataBaseConnectPool;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import DataBaseConnectPool.ORM.*;
import org.junit.Test;

import static DataBaseConnectPool.ORM.SQLGenerator.*;

/**
 * Data Access Object,数据库访问对象
 */
public class Dao {
    public static Logger log = Logger.getLogger(Dao.class.getName());
    // 数据库连接池
    private static ConnectionPool pool;
    //连接
    private Connection conn;
    // 单例模式，获取数据库连接池
    public static ConnectionPool getConnectionPool() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    /**
     * 获取数据库连接
     */
    public void getConnection() {
        if (conn == null) {
            try {
                conn = getConnectionPool().getConnection();
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
                getConnectionPool().releaseConnection(conn);
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
        }finally {
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
    public ResultSet executeQuery(String sql) {
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
        String sql = generateInsertSQL(obj);
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
        String sql = generateUpdateSQL(obj);
        // 执行SQL更新语句
        return upDate(sql);
    }

    /**
     * 传入对象，执行SQL查询语句
     * @param entityClass 对象类
     * @param columns 要查询的列名数组
     * @param where 条件语句
     * @return 结果集
     */
    public ResultSet select(Class<?> entityClass, String[] columns, String where) {
        // 编写SQL语句
        String sql = generateSelectSQL(entityClass, columns, where);
        // 执行SQL查询语句
        return executeQuery(sql);
    }

    /**
     * 传入对象，执行SQL查询语句
     * @param entityClass 对象类
     * @param where 条件语句
     * @return 结果集
     */
    public ResultSet select(Class<?> entityClass, String where) {
        // 编写SQL语句
        String sql = generateSelectSQL(entityClass, where);
        // 执行SQL查询语句
        return executeQuery(sql);
    }

    //以下是测试代码
    @Test
    public void test() throws IllegalAccessException {
        String tableName = "user";
        String[] columns = {"id", "name", "age"};
        String where = "id = 1";
        TestClass testClass = new TestClass(2, "张三", 20);
        String sql = generateSelectSQL(TestClass.class, columns, where);
        System.out.println(sql);

        String sql2 = generateSelectSQL(TestClass.class, where);
        System.out.println(sql2);

        String sql3 = generateInsertSQL(testClass);
        System.out.println(sql3);

        String sql4 = generateUpdateSQL(testClass);
        System.out.println(sql4);
    }

    /**
     * 传入对象，执行SQL查询语句
     * @param entityClass 对象类
     * @return 对象
     */
    public <T> T selectClass(Class<?> entityClass,String where) {
        //获取结果集
        ResultSet rs = select(entityClass, where);
        //将结果集转换为对象
        try {
            return (T) ResultSetToObject.resultSetToObject(rs, entityClass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
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
            while (rs.next()) {
                //将结果集的每一行转换为对象
                T obj = (T) ResultSetToObject.resultSetToObject(rs, entityClass);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        //将结果集转换为对象列表
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
