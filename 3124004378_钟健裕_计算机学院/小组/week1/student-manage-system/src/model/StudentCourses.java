package model;

import DataBaseConnectPool.ORM.AutoPlusId;
import DataBaseConnectPool.ORM.Column;
import DataBaseConnectPool.ORM.DataEntity;
import DataBaseConnectPool.ORM.Id;

@DataEntity(tableName = "student_courses")
public class StudentCourses {
    @Id
    @AutoPlusId
    @Column(name = "student_courses_id")
    private int studentCoursesId;

    @Column(name = "student_id")
    private int studentId;

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "student_num")
    private String studentNum;


    @Column(name = "teacher_name")
    private String teacherName;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "is_del")
    private int isDel;

    public StudentCourses() {}


    public StudentCourses(int studentCoursesId, int studentId, int courseId, String studentName, String teacherName, String courseName) {
        this.studentCoursesId = studentCoursesId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.studentName = studentName;
        this.teacherName = teacherName;
        this.courseName = courseName;
    }

    public StudentCourses(int studentCoursesId, int studentId, int courseId, String studentName, String studentNum, String teacherName, String courseName) {
        this.studentCoursesId = studentCoursesId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.studentName = studentName;
        this.studentNum = studentNum;
        this.teacherName = teacherName;
        this.courseName = courseName;
    }

    public StudentCourses(int studentCoursesId, int studentId, int courseId, String studentName, String studentNum, String teacherName, String courseName, int isDel) {
        this.studentCoursesId = studentCoursesId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.studentName = studentName;
        this.studentNum = studentNum;
        this.teacherName = teacherName;
        this.courseName = courseName;
        this.isDel = isDel;
    }

    /**
     * 获取
     * @return studentCoursesId
     */
    public int getStudentCoursesId() {
        return studentCoursesId;
    }

    /**
     * 设置
     * @param studentCoursesId
     */
    public void setStudentCoursesId(int studentCoursesId) {
        this.studentCoursesId = studentCoursesId;
    }

    /**
     * 获取
     * @return studentId
     */
    public int getStudentId() {
        return studentId;
    }

    /**
     * 设置
     * @param studentId
     */
    public void setStudentId(int studentId) {
        this.studentId = studentId;
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
     * @return studentName
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * 设置
     * @param studentName
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
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
     * @return studentNum
     */
    public String getStudentNum() {
        return studentNum;
    }

    /**
     * 设置
     * @param studentNum
     */
    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String toString() {
        return "StudentCourses{studentCoursesId = " + studentCoursesId + ", studentId = " + studentId + ", courseId = " + courseId + ", studentName = " + studentName + ", studentNum = " + studentNum + ", teacherName = " + teacherName + ", courseName = " + courseName + "}";
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
