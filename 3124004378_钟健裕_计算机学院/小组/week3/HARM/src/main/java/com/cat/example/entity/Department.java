package com.cat.example.entity;

import com.cat.example.dao.ORM.Column;
import com.cat.example.dao.ORM.Entity;
import com.cat.example.dao.ORM.Id;

import java.time.LocalDateTime;

@Entity(tableName = "tb_department")
public class Department {
    @Id
    @Column(name = "dept_id")
    private long deptId;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "description")
    private String description;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name="is_del")
    private byte isDel;

    public Department() {
    }

    public Department(long deptId, String deptName, String description, LocalDateTime createTime) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.description = description;
        this.createTime = createTime;
    }

    public Department(long deptId, String deptName, String description, LocalDateTime createTime, byte isDel) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.description = description;
        this.createTime = createTime;
        this.isDel = isDel;
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
     * @return deptName
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * 设置
     * @param deptName
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * 获取
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
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
        return "Department{deptId = " + deptId + ", deptName = " + deptName + ", description = " + description + ", createTime = " + createTime + "}";
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
