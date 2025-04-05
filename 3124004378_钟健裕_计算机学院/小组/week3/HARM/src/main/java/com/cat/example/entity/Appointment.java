package com.cat.example.entity;

import com.cat.example.dao.ORM.Column;
import com.cat.example.dao.ORM.Entity;
import com.cat.example.dao.ORM.Id;

import java.time.LocalDateTime;

@Entity(tableName="tb_appointment")
public class Appointment {
    @Id
    @Column(name="record_id")
    private long recordId;

    @Column(name="user_id")
    private long userId;

    @Column(name="doctor_id")
    private long doctorId;

    @Column(name="slot_id")
    private long slotId;

    @Column(name="status")
    private byte status;

    @Column(name="create_time")
    private LocalDateTime createTime;

    @Column(name="is_del")
    private byte isDel;


    public Appointment() {
    }

    public Appointment(long recordId, long userId, long doctorId, long slotId, byte status, LocalDateTime createTime, byte isDel) {
        this.recordId = recordId;
        this.userId = userId;
        this.doctorId = doctorId;
        this.slotId = slotId;
        this.status = status;
        this.createTime = createTime;
        this.isDel = isDel;
    }

    /**
     * 获取
     * @return recordId
     */
    public long getRecordId() {
        return recordId;
    }

    /**
     * 设置
     * @param recordId
     */
    public void setRecordId(long recordId) {
        this.recordId = recordId;
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
     * @return slotId
     */
    public long getSlotId() {
        return slotId;
    }

    /**
     * 设置
     * @param slotId
     */
    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }

    /**
     * 获取
     * @return status
     */
    public byte getStatus() {
        return status;
    }

    /**
     * 设置
     * @param status
     */
    public void setStatus(byte status) {
        this.status = status;
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

    public String toString() {
        return "Appointment{recordId = " + recordId + ", userId = " + userId + ", doctorId = " + doctorId + ", slotId = " + slotId + ", status = " + status + ", createTime = " + createTime + ", isDel = " + isDel + "}";
    }
}
