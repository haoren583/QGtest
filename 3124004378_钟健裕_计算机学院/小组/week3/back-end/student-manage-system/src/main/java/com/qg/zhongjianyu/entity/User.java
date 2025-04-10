package com.qg.zhongjianyu.entity;

import com.qg.zhongjianyu.constant.StatusCode;
import com.qg.zhongjianyu.dao.ORM.Column;
import com.qg.zhongjianyu.dao.ORM.DataAuto;
import com.qg.zhongjianyu.dao.ORM.Entity;
import com.qg.zhongjianyu.dao.ORM.Id;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;


/**
 * tb_user的实体类
 * @author 钟健裕
 */
@Entity(tableName="users")
public class User {



    //id
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    @Column(name="user_id")
    protected long userId=0;

    //密码
    @Column(name="password")
    protected String password="";

    //手机号
    @Column(name="phone")
    protected String phone="";

    //姓名
    @Column(name="user_name")
    protected String userName="";


    //创建时间
    @DataAuto
    @Column(name="create_time")
    protected LocalDateTime createTime;


    @Column(name="is_del")
    private byte isDel=0;

    //状态码
    @Column(name="status")
    private int status=0;


    public User() {
    }

    public User(long userId, String password, String phone, String userName, LocalDateTime createTime, byte isDel, int status) {
        this.userId = userId;
        this.password = password;
        this.phone = phone;
        this.userName = userName;
        this.createTime = createTime;
        this.isDel = isDel;
        this.status = status;
    }

    /**
     * 获取
     * @return userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(long userId) {
        this.userId = userId;
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
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取
     * @return createTime
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return isDel
     */
    public byte getIsDel() {
        return isDel;
    }

    /**
     * 设置
     * @param isDel
     */
    public void setIsDel(byte isDel) {
        this.isDel = isDel;
    }

    /**
     * 获取
     * @return status
     */
    public int getStatus() {
        return status;
    }

    /**
     * 设置
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    public String toString() {
        return "User{userId = " + userId + ", password = " + password + ", phone = " + phone + ", userName = " + userName + ", createTime = " + createTime + ", isDel = " + isDel + ", status = " + status + "}";
    }
}
