package model;

import DataBaseConnectPool.Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            Threads.threadPool.submit(new Runnable() {
                @Override
                public void run() {

                }
            });
            List<Course> selectableCourseList = null;
            // 从数据库中查询可选课程
            Dao dao = new Dao();
            selectableCourseList = dao.selectList(Course.class, "amount_seleted_students<volume_students , is_del=0");
            if (selectableCourseList != null) {
                for (Course course : selectableCourseList) {
                    System.out.println(course.toString());
                }
            } else {
                System.out.println("暂无可选课程");
            }
            //输入E退出
            System.out.println("输入E退出");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equals("E")) {
                scanner.close();
                try {
                    operate();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
    }

    /**
     * 选课
     */
    public void SelectCourse() throws IllegalAccessException {
        if(student.getAmountSelectCourse()<5) {
            QuerySelectableCourse();
            Scanner scanner = new Scanner(System.in);
            System.out.println("选课");
            System.out.println("请输入你要选的课程编号：");
            // 从控制台获取输入的课程编号
            int courseId = scanner.nextInt();
            // 从数据库中查询课程信息
            Dao dao = new Dao();
            Course course = dao.selectClass(Course.class, "course_id=" + courseId+" , is_del=0");
            if (course!= null) {
                // 课程存在，判断数量是否已满
                if (course.getVolumeStudents()>course.getAmountSeletedStudents()) {
                    // 未满，则更新学生选课信息
                    student.setAmountSelectCourse(student.getAmountSelectCourse()+1);
                    dao.change(student);
                    // 更新课程选课信息
                    StudentCourses studentCourses=new StudentCourses(0,student.getUserId(),course.getCourseId(),student.getUsername(),student.getStudentNum(),course.getTeacherName(),course.getCourseName());
                    dao.add(studentCourses);
                    course.setAmountSeletedStudents(course.getAmountSeletedStudents()+1);
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
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("E")) {
            scanner.close();
            operate();
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
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("E")) {
            scanner.close();
            operate();
        }
    }
    /**
     * 退课
     */
    public void CancelCourse() throws IllegalAccessException {
        // 从数据库中查询学生选课信息
        Dao dao=new Dao();
        Scanner scanner=new Scanner(System.in);
        List<StudentCourses> studentCoursesList=dao.selectList(StudentCourses.class,"student_id="+student.getUserId());
        if(studentCoursesList!=null||studentCoursesList.size()>0){
            System.out.println("退课");
            for(StudentCourses studentCourses:studentCoursesList){
                System.out.println(studentCourses.toString());
            }
            System.out.println("请输入你要退的课程编号：");
            // 从控制台获取输入的课程编号
            int courseId=scanner.nextInt();
            for(StudentCourses studentCourses:studentCoursesList){
                if(studentCourses.getCourseId()==courseId){
                    // 找到要退的课程，更新学生选课信息
                    student.setAmountSelectCourse(student.getAmountSelectCourse()-1);
                    dao.change(student);
                    Course course=dao.selectClass(Course.class,"course_id="+studentCourses.getCourseId());
                    course.setAmountSeletedStudents(course.getAmountSeletedStudents()-1);
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
            scanner.close();
            operate();
        }
    }
    /**
     * 修改email
     */
    public void changeEmail() throws IllegalAccessException {
        // 从控制台获取输入的新email
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入新email：");
        String newEmail=scanner.next();
        // 更新学生信息
        student.setEmail(newEmail);
        Dao dao=new Dao();
        dao.change(student);
        System.out.println("修改成功");
    }

    public void operate() throws IllegalAccessException {
        Scanner scanner = new Scanner(System.in);
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

            choice = scanner.nextInt();
            // 清除缓冲区
            scanner.nextLine();

            switch (choice) {
                case 1:
                    QuerySelectableCourse();
                    break;
                case 2:
                    // 实现选择课程的功能
                    SelectCourse();
                    System.out.println("选择课程");
                    break;
                case 3:
                    // 实现退选课程的功能
                    CancelCourse();
                    System.out.println("退选课程");
                    break;
                case 4:
                    // 实现查看已选课程的功能
                    ViewSelectedCourse();
                    System.out.println("查看已选课程");
                    break;
                case 5:
                    // 实现修改手机号的功能
                    changeEmail();
                    System.out.println("修改手机号");
                    break;
                case 6:
                    // 退出学生操作
                    System.out.println("退出");
                    break;
                default:
                    System.out.println("无效的选择，请输入 1-6");
            }
        } while (choice != 6);
    }


    public StudentOperate(Student student) {
        this.student = student;
    }
}
