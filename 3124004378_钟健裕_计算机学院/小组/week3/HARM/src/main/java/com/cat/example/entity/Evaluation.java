package com.cat.example.entity;

import com.cat.example.dao.ORM.Column;
import com.cat.example.dao.ORM.Entity;
import com.cat.example.dao.ORM.Id;

import java.time.LocalDateTime;

@Entity(tableName = "tb_evaluation")
public class Evaluation {
    @Id
    @Column(name = "eval_id")
    private long evalId=0;

    @Column(name = "user_id")
    private long userId=0;

    @Column(name = "doctor_id")
    private long doctorId=0;

    @Column(name = "rating")
    private byte rating=5;

    @Column(name = "comment")
    private String comment="";

    @Column(name = "audit_status")
    private byte auditStatus=1;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name="is_del")
    private byte isDel=0;


    public Evaluation() {
    }

    public Evaluation(long evalId, long userId, long doctorId, byte rating, String comment, byte auditStatus, LocalDateTime createTime) {
        this.evalId = evalId;
        this.userId = userId;
        this.doctorId = doctorId;
        this.rating = rating;
        this.comment = comment;
        this.auditStatus = auditStatus;
        this.createTime = createTime;
    }

    public Evaluation(long evalId, long userId, long doctorId, byte rating, String comment, byte auditStatus, LocalDateTime createTime, byte isDel) {
        this.evalId = evalId;
        this.userId = userId;
        this.doctorId = doctorId;
        this.rating = rating;
        this.comment = comment;
        this.auditStatus = auditStatus;
        this.createTime = createTime;
        this.isDel = isDel;
    }

    /**
     * 获取
     * @return evalId
     */
    public long getEvalId() {
        return evalId;
    }

    /**
     * 设置
     * @param evalId
     */
    public void setEvalId(long evalId) {
        this.evalId = evalId;
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
     * @return doctorId
     */
    public long getDoctorId() {
        return doctorId;
    }

    /**
     * 设置
     * @param doctorId
     */
    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * 获取
     * @return rating
     */
    public byte getRating() {
        return rating;
    }

    /**
     * 设置
     * @param rating
     */
    public void setRating(byte rating) {
        this.rating = rating;
    }

    /**
     * 获取
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 获取
     * @return auditStatus
     */
    public byte getAuditStatus() {
        return auditStatus;
    }

    /**
     * 设置
     * @param auditStatus
     */
    public void setAuditStatus(byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    /**
     * 获取
     *
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
        return "Evaluation{evalId = " + evalId + ", userId = " + userId + ", doctorId = " + doctorId + ", rating = " + rating + ", comment = " + comment + ", auditStatus = " + auditStatus + ", createTime = " + createTime + "}";
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
}
