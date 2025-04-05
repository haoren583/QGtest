package com.cat.example.entity;

import com.cat.example.dao.ORM.Column;
import com.cat.example.dao.ORM.Entity;
import com.cat.example.dao.ORM.Id;

import java.time.LocalDateTime;

@Entity(tableName="tb_appointment_slot")
public class AppointmentSlot {
    @Id
    @Column(name="slot_id")
    private long slotId;

    @Column(name="doctor_id")
    private long doctorId;

    @Column(name="date")
    private String date;

    @Column(name="time_range")
    private String timeRange;

    @Column(name="max_capacity")
    private int maxCapacity;

    @Column(name="current_capacity")
    private int currentCapacity;

    @Column(name="status")
    private byte status;

    @Column(name="create_time")
    private LocalDateTime createTime;

    @Column(name="is_del")
    private byte isDel;


    public AppointmentSlot() {
    }

    public AppointmentSlot(long slotId, long doctorId, String date, String timeRange, int maxCapacity, int currentCapacity, byte status, LocalDateTime createTime) {
        this.slotId = slotId;
        this.doctorId = doctorId;
        this.date = date;
        this.timeRange = timeRange;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
        this.status = status;
        this.createTime = createTime;
    }

    public AppointmentSlot(long slotId, long doctorId, String date, String timeRange, int maxCapacity, int currentCapacity, byte status, LocalDateTime createTime, byte isDel) {
        this.slotId = slotId;
        this.doctorId = doctorId;
        this.date = date;
        this.timeRange = timeRange;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
        this.status = status;
        this.createTime = createTime;
        this.isDel = isDel;
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
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * 设置
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 获取
     * @return timeRange
     */
    public String getTimeRange() {
        return timeRange;
    }

    /**
     * 设置
     * @param timeRange
     */
    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    /**
     * 获取
     * @return maxCapacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * 设置
     * @param maxCapacity
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * 获取
     * @return currentCapacity
     */
    public int getCurrentCapacity() {
        return currentCapacity;
    }

    /**
     * 设置
     * @param currentCapacity
     */
    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
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

    public String toString() {
        return "AppointmentSlot{slotId = " + slotId + ", doctorId = " + doctorId + ", date = " + date + ", timeRange = " + timeRange + ", maxCapacity = " + maxCapacity + ", currentCapacity = " + currentCapacity + ", status = " + status + ", createTime = " + createTime + "}";
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
