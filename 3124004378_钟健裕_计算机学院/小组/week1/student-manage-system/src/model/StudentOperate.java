package model;

import DataBaseConnectPool.Dao;

import java.util.List;
import java.util.Scanner;

import static model.MyScanner.scanner;

/**
 * 学生用户可执行的操作
 */
public class StudentOperate {
    private Student student;
    /**
     * 查找可选的课程
     */
    public void QuerySelectableCourse(){
            System.out.println("可选课程");
            List<Course> selectableCourseList = null;
            // 从数据库中查询可选课程
            Dao dao = new Dao();
            selectableCourseList = dao.selectList(Course.class, "amount_selected_students<volume_students and is_del=0");
            if (selectableCourseList != null) {
                for (Course course : selectableCourseList) {
                    System.out.println(course.toString());
                }
            } else {
                System.out.println("暂无可选课程");
            }
            //输入E退出
            System.out.println("输入E退出");
            String input = scanner.nextLine();
            if (input.equals("E")) {
                System.out.println("===== 学生菜单 =====");
                System.out.println("1. 查看可选课程");
                System.out.println("2. 选择课程");
                System.out.println("3. 退选课程");
                System.out.println("4. 查看已选课程");
                System.out.println("5. 修改手机号");
                System.out.println("6. 退出");
                System.out.println("请选择操作（输入 1-6）：");
                return;
            }
    }

    /**
     * 选课
     */
    public void SelectCourse() throws IllegalAccessException {
        if(student.getAmountSelectCourse()<5) {
            QuerySelectableCourse();
            System.out.println("选课");
            System.out.println("请输入你要选的课程编号：");
            // 从控制台获取输入的课程编号
            String courseId=scanner.nextLine();
            // 从数据库中查询课程信息
            Dao dao = new Dao();
            Course course = dao.selectClass(Course.class, "course_id=" + courseId+" , is_del=0");
            if (course!= null) {
                // 课程存在，判断数量是否已满
                if (course.getVolumeStudents()>course.getAmountSelectedStudents()) {
                    // 未满，则更新学生选课信息
                    student.setAmountSelectCourse(student.getAmountSelectCourse()+1);
                    dao.change(student);
                    // 更新课程选课信息
                    StudentCourses studentCourses=new StudentCourses(0,student.getUserId(),course.getCourseId(),student.getUsername(),student.getStudentNum(),course.getTeacherName(),course.getCourseName());
                    dao.add(studentCourses);
                    course.setAmountSelectedStudents(course.getAmountSelectedStudents()+1);
                    dao.change(course);
                    System.out.println("选课成功");
                }else{
                    System.out.println("该课程已被选满");
                }
            }
        }else{
            System.out.println("你已选满5门课程");
        }
        // 输入E退出
        System.out.println("输入E退出");
        String input = scanner.nextLine();
        if (input.equals("E")) {
            System.out.println("===== 学生菜单 =====");
            System.out.println("1. 查看可选课程");
            System.out.println("2. 选择课程");
            System.out.println("3. 退选课程");
            System.out.println("4. 查看已选课程");
            System.out.println("5. 修改手机号");
            System.out.println("6. 退出");
            System.out.println("请选择操作（输入 1-6）：");
            return;
        }
    }
    /**
     * 查看已选课程
     */
    public void ViewSelectedCourse() throws IllegalAccessException {
        // 从数据库中查询学生选课信息
        Dao dao=new Dao();
        List<StudentCourses> studentCoursesList=dao.selectList(StudentCourses.class,"student_id="+student.getUserId()+" ,is_del=0");
        if(studentCoursesList!=null||studentCoursesList.size()>0){
            System.out.println("已选课程");
            for(StudentCourses studentCourses:studentCoursesList){
                System.out.println(studentCourses.toString());
            }
        }else{
            System.out.println("你还没有选课");
        }
        // 输入E退出
        System.out.println("输入E退出");
        String input = scanner.nextLine();
        if (input.equals("E")) {
            System.out.println("===== 学生菜单 =====");
            System.out.println("1. 查看可选课程");
            System.out.println("2. 选择课程");
            System.out.println("3. 退选课程");
            System.out.println("4. 查看已选课程");
            System.out.println("5. 修改手机号");
            System.out.println("6. 退出");
            System.out.println("请选择操作（输入 1-6）：");
            return;
        }
    }
    /**
     * 退课
     */
    public void CancelCourse() throws IllegalAccessException {
        // 从数据库中查询学生选课信息
        Dao dao=new Dao();
        List<StudentCourses> studentCoursesList=dao.selectList(StudentCourses.class,"student_id="+student.getUserId());
        if(studentCoursesList!=null||studentCoursesList.size()>0){
            System.out.println("退课");
            for(StudentCourses studentCourses:studentCoursesList){
                System.out.println(studentCourses.toString());
            }
            System.out.println("请输入你要退的课程编号：");
            // 从控制台获取输入的课程编号
            String courseId=scanner.nextLine();

            for(StudentCourses studentCourses:studentCoursesList){
                if(String.valueOf(studentCourses.getCourseId()).equals(courseId)){
                    // 找到要退的课程，更新学生选课信息
                    student.setAmountSelectCourse(student.getAmountSelectCourse()-1);
                    dao.change(student);
                    Course course=dao.selectClass(Course.class,"course_id="+studentCourses.getCourseId());
                    course.setAmountSelectedStudents(course.getAmountSelectedStudents()-1);
                    dao.change(course);
                    //删除学生选课信息
                    studentCourses.setIsDel(1);
                    dao.change(studentCourses);
                    System.out.println("退课成功");
                    break;
                }
            }
        }else{
            System.out.println("你还没有选课");
        }
        // 输入E退出
        System.out.println("输入E退出");
        String input = scanner.nextLine();
        if (input.equals("E")) {
            System.out.println("===== 学生菜单 =====");
            System.out.println("1. 查看可选课程");
            System.out.println("2. 选择课程");
            System.out.println("3. 退选课程");
            System.out.println("4. 查看已选课程");
            System.out.println("5. 修改手机号");
            System.out.println("6. 退出");
            System.out.println("请选择操作（输入 1-6）：");
            return;
        }
    }
    /**
     * 修改email
     */
    public void changeEmail() throws IllegalAccessException {
        // 从控制台获取输入的新email
        System.out.println("请输入新email：");
        String newEmail=scanner.nextLine();
        // 更新学生信息
        student.setEmail(newEmail);
        Dao dao=new Dao();
        User user=dao.selectClass(User.class,"user_id="+student.getUserId());
        user.setEmail(newEmail);
        dao.change(user);
        System.out.println("修改成功");
        return;
    }

    public void operate() throws IllegalAccessException {
        int choice;

        do {
            System.out.println("===== 学生菜单 =====");
            System.out.println("1. 查看可选课程");
            System.out.println("2. 选择课程");
            System.out.println("3. 退选课程");
            System.out.println("4. 查看已选课程");
            System.out.println("5. 修改手机号");
            System.out.println("6. 退出");
            System.out.println("请选择操作（输入 1-6）：");

            String input = scanner.nextLine();
            if(!input.matches("\\d+")){
                System.out.println("无效的选择，请输入 1-6");
                continue;
            }else {
                choice = Integer.parseInt(input);
            }

            switch (choice) {
                case 1:
                    QuerySelectableCourse();
                    break;
                case 2:
                    // 实现选择课程的功能
                    SelectCourse();
                    break;
                case 3:
                    // 实现退选课程的功能
                    CancelCourse();
                    break;
                case 4:
                    // 实现查看已选课程的功能
                    ViewSelectedCourse();
                    break;
                case 5:
                    // 实现修改手机号的功能
                    changeEmail();
                    break;
                case 6:
                    // 退出学生操作
                    System.out.println("退出");
                    System.out.println("=======================================");
                    System.out.println("学生选课管理系统");
                    System.out.println("=======================================");
                    System.out.println("1. 登录");
                    System.out.println("2. 注册");
                    System.out.println("3. 退出");
                    System.out.println("请选择操作（输入 1-3）：");
                    return;
                default:
                    System.out.println("无效的选择，请输入 1-6");
            }
        } while (true);
    }


    public StudentOperate(Student student) {
        this.student = student;
    }
}
