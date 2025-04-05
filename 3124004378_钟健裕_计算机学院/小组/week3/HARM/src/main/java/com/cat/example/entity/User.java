package com.cat.example.entity;

import com.cat.example.constant.StatusCode;
import com.cat.example.dao.ORM.Column;
import com.cat.example.dao.ORM.DataAuto;
import com.cat.example.dao.ORM.Entity;
import com.cat.example.dao.ORM.Id;

import java.time.LocalDateTime;


/**
 * tb_user的实体类
 * @author 钟健裕
 */
@Entity(tableName="tb_user")
public class User {



    //id
    @Id
    @Column(name="user_id")
    protected long userId=0;

    //学号或工号
    @Column(name="card_id")
    protected String cardId = "";
    //密码
    @Column(name="password")
    protected String password="";

    //手机号
    @Column(name="phone")
    protected String phone="";

    //姓名
    @Column(name="user_name")
    protected String userName="";

    //性别
    //"UNKNOWN","MALE","FEMALE"
    @Column(name="gender")
    protected String gender="UNKNOWN";

    //状态,0表示未认证，1表示已认证
    @Column(name="status")
    protected int status=0;

    //角色。Common，Doctor，Admin，DoctorAndAdmin
    @Column(name="role")
    protected String role="Common";

    //创建时间
    @DataAuto
    @Column(name="create_time")
    protected LocalDateTime createTime;

    //更新时间
    @DataAuto
    @Column(name="update_time")
    protected LocalDateTime updateTime;

    @Column(name="is_del")
    private byte isDel=0;

    public User() {
    }

    public User(long userId, String cardId, String password, String phone, String userName, String gender, int status, String role, LocalDateTime createTime, LocalDateTime updateTime, byte isDel) {
        this.userId = userId;
        this.cardId = cardId;
        this.password = password;
        this.phone = phone;
        this.userName = userName;
        this.gender = gender;
        this.status = status;
        this.role = role;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDel = isDel;
    }

    /**
     * 获取
     * @return id
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
     * @return cardId
     */
    public String getCardId() {
        return cardId;
    }

    /**
     * 设置
     * @param cardId
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
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
     * @return name
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
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * 设置
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
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
     * @param statusCode
     */
    public void setStatus(StatusCode statusCode, boolean is) {
        if (is) {
            this.status |= (1 << statusCode.getValue());
        }
        else {
            this.status &= ~(1 << statusCode.getValue());
        }
    }

    /**
     * 获取
     *
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * 设置
     * @param role 角色。Common，Doctor，Admin，DoctorAndAdmin
     */
    public void setRole(String role) {
        this.role = role;
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
     * @return updateTime
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置
     * @param updateTime
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
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

    public String toString() {
        return "User{userId = " + userId + ", cardId = " + cardId + ", password = " + password + ", phone = " + phone + ", userName = " + userName + ", gender = " + gender + ", status = " + status + ", role = " + role + ", createTime = " + createTime + ", updateTime = " + updateTime + ", isDel = " + isDel + "}";
    }


}
