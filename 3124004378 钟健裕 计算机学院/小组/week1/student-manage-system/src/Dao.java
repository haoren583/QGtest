// database access object，数据访问对象，简化对数据库的操作过程。

import DataBaseConnectPool.Preprocessor;
import org.junit.Test;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Dao {
    public static final Logger log = Logger.getLogger(Dao.class.getName());
    private Connection conn;
    //private Connection connBatch;
    private PreparedStatement ps;
    private ResultSet rs;

    public Dao() throws SQLException {
        //连接数据库
        connect();
        //connectBatch();
        ps=null;
        rs=null;
    }

    //创建数据库连接
    public void connect() {
        log.info("connect");
        try {
            if (conn == null || conn.isClosed()) {
                conn=DriverManager.getConnection(Preprocessor.getUrl(), Preprocessor.getUsername(), Preprocessor.getPassword());
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //创建批量更新连接
    public void connectBatch() {
        log.info("connectBatch");
        try {
            if (conn == null || conn.isClosed()) {
                conn=DriverManager.getConnection(Preprocessor.getConnectionBatchUrl(), Preprocessor.getUsername(), Preprocessor.getPassword());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //释放资源
    public void close() {
        log.info("close");
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null &&!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //执行查询语句
    public ResultSet executeQuery(String sql) {
        log.info("executeQuery: " + sql);
        if(sql==null){
            return null;
        }
        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }

    //执行更新语句,返回受影响的行数
    public int update(String sql,Object... params) {
        log.info("update: " + sql+" params: "+ Arrays.toString(params));
        if(sql==null){
            return -1;
        }
        try{
            ps = conn.prepareStatement(sql);
            if(params!=null){
                //设置参数
                for(int i=0;i<params.length;i++){
                    ps.setObject(i+1, params[i]);
                }
            }
            //释放资源
            close();
            log.info("update: "+ps.executeUpdate());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //执行批量更新语句,返回受影响的行数
    public int[] batchUpdate(String sql,List<Object[]> params) {
        log.info("batchUpdate: " + sql+" params: "+ params);
        if(sql==null){
            return null;
        }
        //清除原连接
        try {
            if(!(conn==null||conn.isClosed())) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //创建新的连接
        connectBatch();
        //执行批量更新
        try {
            ps = conn.prepareStatement(sql);
            int[] result = new int[params.size()];
            for (int i = 0; i < params.size(); i++) {
                Object[] param = params.get(i);
                for (int j = 0; j < param.length; j++) {
                    ps.setObject(j + 1, param[j]);
                }
                result[i] = ps.executeUpdate();
            }
            //释放资源
            close();
            log.info("batchUpdate: "+Arrays.toString(result));
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    //批量增加数据

    //这是测试用函数
    @Test
    public void testDao() throws SQLException {
        Dao dao = new Dao();
        ResultSet rs = dao.executeQuery("select * from users");
        while (rs.next()) {
            System.out.println(rs.getString("username"));
        }
        System.out.println("testDao success");
        Object[] params = {"钟健裕", "111111", "ADMIN", "111111@qq.com"};
        dao.update("INSERT INTO users(username, password, role_type, email) VALUES (?,?,?,?)", params);
    }
}