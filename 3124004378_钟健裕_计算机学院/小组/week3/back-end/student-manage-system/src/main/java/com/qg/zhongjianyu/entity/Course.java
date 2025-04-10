package com.qg.zhongjianyu.entity;

import com.qg.zhongjianyu.dao.Dao;
import com.qg.zhongjianyu.dao.ORM.Column;
import com.qg.zhongjianyu.dao.ORM.DataAuto;
import com.qg.zhongjianyu.dao.ORM.Entity;
import com.qg.zhongjianyu.dao.ORM.Id;
import com.qg.zhongjianyu.util.UUID;

import java.time.LocalDateTime;

@Entity(tableName = "courses")
public class Course {
    @Id
    @Column(name = "course_id")
    private Long courseId;
    @Column(name = "course_name")
    private String courseName;
    @Column(name = "teacher_name")
    private String teacherName;
    @Column(name = "volume_students")
    private int volumeStudents;
    @Column(name = "amount_selected_students")
    private int amountSelectedStudents;
    @Column(name = "score")
    public int  score;
    @Column(name = "is_del")
    private int isDel=0;
    @DataAuto
    @Column(name = "create_time")
    private LocalDateTime createTime;

    public Course() {
    }

    public Course(Long courseId, String courseName, String teacherName, int volumeStudents, int amountSelectedStudents, int score, int isDel, LocalDateTime createTime) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.volumeStudents = volumeStudents;
        this.amountSelectedStudents = amountSelectedStudents;
        this.score = score;
        this.isDel = isDel;
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return courseId
     */
    public Long getCourseId() {
        return courseId;
    }

    /**
     * 设置
     * @param courseId
     */
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    /**
     * 获取
     * @return courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * 设置
     * @param courseName
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * 获取
     * @return teacherName
     */
    public String getTeacherName() {
        return teacherName;
    }

    /**
     * 设置
     * @param teacherName
     */
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    /**
     * 获取
     * @return volumeStudents
     */
    public int getVolumeStudents() {
        return volumeStudents;
    }

    /**
     * 设置
     * @param volumeStudents
     */
    public void setVolumeStudents(int volumeStudents) {
        this.volumeStudents = volumeStudents;
    }

    /**
     * 获取
     * @return amountSelectedStudents
     */
    public int getAmountSelectedStudents() {
        return amountSelectedStudents;
    }

    /**
     * 设置
     * @param amountSelectedStudents
     */
    public void setAmountSelectedStudents(int amountSelectedStudents) {
        this.amountSelectedStudents = amountSelectedStudents;
    }

    /**
     * 获取
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * 设置
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
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
        return "Course{courseId = " + courseId + ", courseName = " + courseName + ", teacherName = " + teacherName + ", volumeStudents = " + volumeStudents + ", amountSelectedStudents = " + amountSelectedStudents + ", score = " + score + ", isDel = " + isDel + ", createTime = " + createTime + "}";
    }


    public static void main(String[] args) throws IllegalAccessException {
        Dao dao = new Dao();
        Course course1 = new Course(UUID.generateUUID(), "语文", "张老师", 100, 80, 90, 0, LocalDateTime.now());
        System.out.println(course1.toString());
        dao.add(course1);
        Course course2=new Course(UUID.generateUUID(), "数学", "李老师", 100, 80, 90, 0, LocalDateTime.now());
        dao.add(course2);
        Course course3 = new Course(UUID.generateUUID(),"英语","李老师",100,80,90,0,LocalDateTime.now());
        dao.add(course3);
        Course course4 = new Course(UUID.generateUUID(), "物理", "李老师", 100, 80, 90, 0, LocalDateTime.now());
        dao.add(course4);

    }
}
