import com.cat.example.dao.Dao;
import com.cat.example.dao.ORM.ResultSetToObject;
import com.cat.example.dao.ORM.SQLGenerator;
import com.cat.example.entity.User;
import com.cat.example.util.ThreadPoolUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Future;

import static com.cat.example.dao.StaticConnectionsPool.CONNECTIONS_POOL;


public class ProjectTest {

    @Test
    public void testConnectionsPools() throws SQLException, InterruptedException {
        for (int i = 0; i < 30; i++){
            Connection conn = null;
            Future<Connection> future = ThreadPoolUtil.EXECUTOR.submit(CONNECTIONS_POOL::getConnection);
            try {
                conn = future.get();
                System.out.println(conn);
                CONNECTIONS_POOL.releaseConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CONNECTIONS_POOL.releaseConnection(conn);
            }
        }
    }

    @Test
    public void testORM() throws SQLException, InterruptedException {
        //查寻指定表的数据
        for (int i = 0; i < 3; i++){
            //生成查询语句
            String sql = SQLGenerator.generateSelectSQL(User.class,"user_id="+i);
            Connection conn = null;
            try {
                conn = CONNECTIONS_POOL.getConnection();
                System.out.println(conn);
                //执行查询语句
                ResultSet resultSet = conn.prepareStatement(sql).executeQuery();
                //处理查询结果
                User user = ResultSetToObject.resultSetToObject(resultSet, User.class);
                System.out.println(user);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CONNECTIONS_POOL.releaseConnection(conn);
            }
        }
    }

    @Test
    public void testDao(){
        Dao dao = new Dao();
        User user = dao.selectClass(User.class, "user_id = 1");
        System.out.println(user);
    }
}
