package com.qg.zhongjianyu.entity;

import com.qg.zhongjianyu.dao.ORM.Column;
import com.qg.zhongjianyu.dao.ORM.Entity;

import java.time.LocalDateTime;

@Entity(tableName = "students")
public class Student {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "amount_selected_courses")
    private int amountSelectedCourses = 0;
    @Column(name = "is_del")
    private int isDel=0;
    @Column(name = "create_time")
    private LocalDateTime createTime;

    public Student() {
    }

    public Student(Long userId, int amountSelectedCourses, int isDel, LocalDateTime createTime) {
        this.userId = userId;
        this.amountSelectedCourses = amountSelectedCourses;
        this.isDel = isDel;
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取
     * @return amountSelectedCourses
     */
    public int getAmountSelectedCourses() {
        return amountSelectedCourses;
    }

    /**
     * 设置
     * @param amountSelectedCourses
     */
    public void setAmountSelectedCourses(int amountSelectedCourses) {
        this.amountSelectedCourses = amountSelectedCourses;
    }

    /**
     * 获取
     * @return isDel
     */
    public int getIsDel() {
        return isDel;
    }

    /**
     * 设置
     * @param isDel
     */
    public void setIsDel(int isDel) {
        this.isDel = isDel;
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

    public String toString() {
        return "Student{userId = " + userId + ", amountSelectedCourses = " + amountSelectedCourses + ", isDel = " + isDel + ", createTime = " + createTime + "}";
    }
}
