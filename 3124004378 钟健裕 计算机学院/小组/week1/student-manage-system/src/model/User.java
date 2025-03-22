package model;

import DataBaseConnectPool.ORM.AutoPlusId;
import DataBaseConnectPool.ORM.Column;
import DataBaseConnectPool.ORM.DataEntity;

/**
 * 用户类
 */
@DataEntity(tableName = "users")
public class User {
    @AutoPlusId
    @Column(name = "user_id")
    private int userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "sex")
    private int sex;

    @Column(name = "role_type")
    private String roleType;

    @Column(name = "is_del")
    private int isDel;

    public User() {
    }

    public User(int userId, String username, String password, String email, int sex, String roleType) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.sex = sex;
        this.roleType = roleType;
    }

    /**
     * 获取
     * @return userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * 获取
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取
     * @return sex
     */
    public int getSex() {
        return sex;
    }

    /**
     * 设置
     * @param sex
     */
    public void setSex(int sex) {
        this.sex = sex;
    }

    /**
     * 获取
     * @return roleType
     */
    public String getRoleType() {
        return roleType;
    }

    /**
     * 设置
     * @param roleType
     */
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String toString() {
        return "User{userId = " + userId + ", username = " + username + ", password = " + password + ", email = " + email + ", sex = " + sex + ", roleType = " + roleType + "}";
    }
}
