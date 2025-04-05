package com.cat.example.entity;

import com.cat.example.dao.ORM.Column;
import com.cat.example.dao.ORM.DataAuto;
import com.cat.example.dao.ORM.Entity;
import com.cat.example.dao.ORM.Id;

import java.time.LocalDateTime;

@Entity(tableName = "tb_doctor")
public class Doctor extends User {

    @Id
    @Column(name = "doctor_id")
    private long doctorId;

    @Column(name = "doctor_name")
    private String doctorName;

    //医生的科室ID
    @Column(name = "dept_id")
    private long deptId;

    //医生的职称
    @Column(name = "title")
    private String title;

    //医生的简介
    @Column(name = "intro")
    private String intro;

    //医生的工作时间
    @Column(name = "schedule")
    private String schedule;

    //医生的在岗状态
    @Column(name = "status")
    private int status=0;

    //创建时间
    @DataAuto
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name="is_del")
    private byte isDel;


    public Doctor() {
    }

    public Doctor(long doctorId, String doctorName, long deptId, String title, String intro, String schedule, int status, LocalDateTime createTime) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.deptId = deptId;
        this.title = title;
        this.intro = intro;
        this.schedule = schedule;
        this.status = status;
        this.createTime = createTime;
    }

    public Doctor(long doctorId, String doctorName, long deptId, String title, String intro, String schedule, int status, LocalDateTime createTime, byte isDel) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.deptId = deptId;
        this.title = title;
        this.intro = intro;
        this.schedule = schedule;
        this.status = status;
        this.createTime = createTime;
        this.isDel = isDel;
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
     * @return doctorName
     */
    public String getDoctorName() {
        return doctorName;
    }

    /**
     * 设置
     * @param doctorName
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    /**
     * 获取
     * @return deptId
     */
    public long getDeptId() {
        return deptId;
    }

    /**
     * 设置
     * @param deptId
     */
    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取
     * @return intro
     */
    public String getIntro() {
        return intro;
    }

    /**
     * 设置
     * @param intro
     */
    public void setIntro(String intro) {
        this.intro = intro;
    }

    /**
     * 获取
     * @return schedule
     */
    public String getSchedule() {
        return schedule;
    }

    /**
     * 设置
     * @param schedule
     */
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    /**
     * @param user
     */
    public void setStatus(User user) {
        this.status = user.getStatus();
    }

    /**
     * 获取
     * @return jobStatus
     */
    @Override
    public int getStatus() {
        return status;
    }

    /**
     * 设置
     * @param user
     */
    public void setJobStatus(User user) {
        this.status = user.getStatus();
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
        return "Doctor{doctorId = " + doctorId + ", doctorName = " + doctorName + ", deptId = " + deptId + ", title = " + title + ", intro = " + intro + ", schedule = " + schedule + ", status = " + status + ", createTime = " + createTime + "}";
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
