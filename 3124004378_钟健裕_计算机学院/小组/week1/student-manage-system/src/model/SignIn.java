package model;

import DataBaseConnectPool.Dao;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * 登录机
 */
public class SignIn {
    public static Logger log = Logger.getLogger(SignIn.class.getName());
    //登录池
    public static HashMap<Integer, User> signInPool = new HashMap<>();

    //登录
    public boolean signIn(User user) {
        Dao dao = new Dao();
        //在数据库中查询用户信息
        try {
            User rightUser = dao.selectClass(User.class, "username='" + user.getUsername() + "' and password='" + user.getPassword() + "'");
            if (rightUser != null) {
                //登录成功
                signInPool.put(rightUser.getUserId(), rightUser);
                return true;
            } else {
                //登录失败
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //登录
    public User signIn(String email, String password) {
        Dao dao = new Dao();
        //在数据库中查询用户信息
        try {
            User rightUser = dao.selectClass(User.class, "email='" + email + "' and password='" + password + "'");
            if (rightUser != null) {
                //登录成功
                signInPool.put(rightUser.getUserId(), rightUser);
                log.info("\n\t"+email
                        +"\n\t登录成功");
                return rightUser;
            } else {
                //登录失败
                log.info("\n\t"+email
                        +"\n\t登录失败");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
