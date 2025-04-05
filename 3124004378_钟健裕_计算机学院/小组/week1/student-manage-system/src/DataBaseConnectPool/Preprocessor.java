package DataBaseConnectPool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author 钟健裕
 */
public class Preprocessor {
    private static final String driverClassName;
    private static final String url;
    private static final String betchUrl;
    private static final String username;
    private static final String password;
    private static final Logger log = Logger.getLogger(Preprocessor.class.getName());
    //private static final int initialSize;
    private static final int maxActive;
    //private static final int maxIdle;
    private static final int freeConnections;
    private static final long maxWait;

    public Preprocessor() {}

    static {
        Properties prop=new Properties();
        try{
            // 加载数据库配置文件
            // 打开文件输入流，加载属性文件内容
            FileInputStream fileInputStream = new FileInputStream("DataBase.properties");
            prop.load(fileInputStream); // 将属性文件内容加载到 Properties 对象中
            fileInputStream.close(); // 关闭文件输入流，释放资源

            // 从 Properties 对象中获取数据库连接信息
            driverClassName=prop.getProperty("driverClassName");
            url =prop.getProperty("url");
            betchUrl=prop.getProperty("batchUrl");
            username=prop.getProperty("username");
            password=prop.getProperty("password");
            //initialSize=Integer.parseInt(prop.getProperty("initialSize"));
            maxActive=Integer.parseInt(prop.getProperty("maxActive"));
            //maxIdle=Integer.parseInt(prop.getProperty("maxIdle"));
            freeConnections =Integer.parseInt(prop.getProperty("minIdle"));
            maxWait=Long.parseLong(prop.getProperty("maxWait"));

            // 加载数据库驱动
            Class.forName(driverClassName);//不需要

            //显示数据库连接信息
            log.info("数据库连接信息：driverClassName="+driverClassName+",url="+url+",username="+username+",password="+password);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("数据库配置文件 DataBase.properties 不存在",e);
        } catch (IOException e) {
            throw new RuntimeException("DataBase.properties 文件读取失败",e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("数据库驱动加载失败",e);
        }
    }

    public static String getDriverClassName() {
        return driverClassName;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getConnectionBatchUrl() {
        return betchUrl;
    }

//    public static int getInitialSize() {
//        return initialSize;
//    }

    public static int getMaxActive() {
        return maxActive;
    }

//    public static int getMaxIdle() {
//        return maxIdle;
//    }

    public static int getFreeConnections() {
        return freeConnections;
    }

    public static long getMaxWait() {
        return maxWait;
    }



//    public static void main(String[] args) throws SQLException {
//        //连接数据库
//        Connection connection = DriverManager.getConnection(url,username,password);
//        log.info("数据库连接成功");
//        //执行SQL语句
//        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(username, password, role_type, email) VALUES (?,?,?,?)");
//        //赋值
//        String username = "haoren"; // 用户名
//        String password = "password"; // 密码（通常需要加密存储）
//        String roleType = "ADMIN"; // 角色类型
//        String email = "email@example.com"; // 邮箱地址
//        preparedStatement.setString(1, username);
//        preparedStatement.setString(2, password);
//        preparedStatement.setString(3, roleType);
//        preparedStatement.setString(4, email);
//        preparedStatement.executeUpdate();
//        log.info("用户 " + username + " 注册成功");
//        //关闭数据库连接
//        connection.close();
//    }
}
