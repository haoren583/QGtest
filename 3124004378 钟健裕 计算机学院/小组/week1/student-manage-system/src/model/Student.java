package model;


import DataBaseConnectPool.ORM.AutoPlusId;
import DataBaseConnectPool.ORM.Column;
import DataBaseConnectPool.ORM.DataEntity;
import DataBaseConnectPool.ORM.Id;

/**
 * @author 钟健裕
 */
@DataEntity(tableName = "students")
public class Student extends User {

    private String username;

    private String password;

    private String email;

    private int sex;

    private String roleType;

    @Id
    @AutoPlusId
    private int userId;

    @Column(name = "student_num")
    private String studentNum;

    @Column(name = "college_id")
    private int collegeId;

    @Column(name = "major_id")
    private int majorId;

    @Column(name = "grade")
    private int grade;

    @Column(name = "class")
    private int classNum;

    //已选课程数量
    @Column(name = "amount_select_course")
    private int amountSelectCourse;

    @Column(name = "is_del")
    private int isDel;


    public Student() {
    }

    public Student(int userId, String studentNum, int collegeId, int majorId, int grade, int classNum, int amountSelectCourse) {
        this.userId = userId;
        this.studentNum = studentNum;
        this.collegeId = collegeId;
        this.majorId = majorId;
        this.grade = grade;
        this.classNum = classNum;
        this.amountSelectCourse = amountSelectCourse;
    }

    public Student(int userId, String studentNum, int collegeId, int majorId, int grade, int classNum, int amountSelectCourse, int isDel) {
        this.userId = userId;
        this.studentNum = studentNum;
        this.collegeId = collegeId;
        this.majorId = majorId;
        this.grade = grade;
        this.classNum = classNum;
        this.amountSelectCourse = amountSelectCourse;
        this.isDel = isDel;
    }

    public Student(String username, String password, String email, int sex, String roleType, int userId, String studentNum, int collegeId, int majorId, int grade, int classNum, int amountSelectCourse, int isDel) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.sex = sex;
        this.roleType = roleType;
        this.userId = userId;
        this.studentNum = studentNum;
        this.collegeId = collegeId;
        this.majorId = majorId;
        this.grade = grade;
        this.classNum = classNum;
        this.amountSelectCourse = amountSelectCourse;
        this.isDel = isDel;
    }

    /**
     * 获取
     * @return userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
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

    /**
     * 获取
     * @return collegeId
     */
    public int getCollegeId() {
        return collegeId;
    }

    /**
     * 设置
     * @param collegeId
     */
    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    /**
     * 获取
     * @return majorId
     */
    public int getMajorId() {
        return majorId;
    }

    /**
     * 设置
     * @param majorId
     */
    public void setMajorId(int majorId) {
        this.majorId = majorId;
    }

    /**
     * 获取
     * @return grade
     */
    public int getGrade() {
        return grade;
    }

    /**
     * 设置
     * @param grade
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * 获取
     * @return classNum
     */
    public int getClassNum() {
        return classNum;
    }

    /**
     * 设置
     * @param classNum
     */
    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }

    /**
     * 获取
     * @return amountSelectCourse
     */
    public int getAmountSelectCourse() {
        return amountSelectCourse;
    }

    /**
     * 设置
     * @param amountSelectCourse
     */
    public void setAmountSelectCourse(int amountSelectCourse) {
        this.amountSelectCourse = amountSelectCourse;
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
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
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
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取
     * @return sex
     */
    public int getSex() {
        return sex;
    }

    /**
     * 设置
     * @param sex
     */
    public void setSex(int sex) {
        this.sex = sex;
    }

    /**
     * 获取
     * @return roleType
     */
    public String getRoleType() {
        return roleType;
    }

    /**
     * 设置
     * @param roleType
     */
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String toString() {
        return "Student{username = " + username + ", password = " + password + ", email = " + email + ", sex = " + sex + ", roleType = " + roleType + ", userId = " + userId + ", studentNum = " + studentNum + ", collegeId = " + collegeId + ", majorId = " + majorId + ", grade = " + grade + ", classNum = " + classNum + ", amountSelectCourse = " + amountSelectCourse + ", isDel = " + isDel + "}";
    }
}
