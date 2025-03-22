package model;

import DataBaseConnectPool.ORM.Column;
import DataBaseConnectPool.ORM.DataEntity;
import DataBaseConnectPool.ORM.Id;

@DataEntity(tableName = "courses")
public class Course {
    @Id
    @Column(name = "course_id")
    private int courseId;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "teacher_name")
    private String teacherName;

    @Column(name = "volume_students")
    private int volumeStudents;

    @Column(name = "amount_selected_students")
    private int amountSelectedStudents;

    @Column(name = "score")
    private int score;

    @Column(name = "is_del")
    private int isDel;

    public Course(int courseId, String courseName, String teacherName, int volumeStudents, int amountSelectedStudents) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.volumeStudents = volumeStudents;
        this.amountSelectedStudents = amountSelectedStudents;
    }


    public Course() {
    }

    public Course(int courseId, String courseName, String teacherName, int volumeStudents, int amountSelectedStudents, int score) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.volumeStudents = volumeStudents;
        this.amountSelectedStudents = amountSelectedStudents;
        this.score = score;
    }

    public Course(int courseId, String courseName, String teacherName, int volumeStudents, int amountSelectedStudents, int score, int isDel) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.volumeStudents = volumeStudents;
        this.amountSelectedStudents = amountSelectedStudents;
        this.score = score;
        this.isDel = isDel;
    }

    /**
     * 获取
     * @return courseId
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * 设置
     * @param courseId
     */
    public void setCourseId(int courseId) {
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
     * @return amountSeletedStudents
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

    public String toString() {
        return "Course{courseId = " + courseId + ", courseName = " + courseName + ", teacherName = " + teacherName + ", volumeStudents = " + volumeStudents + ", amountSeletedStudents = " + amountSelectedStudents + ", score = " + score + "}";
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
}
