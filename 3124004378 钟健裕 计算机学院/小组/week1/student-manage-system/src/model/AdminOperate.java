package model;

import DataBaseConnectPool.Dao;
import DataBaseConnectPool.ORM.ResultSetToObject;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static model.MyScanner.scanner;

public class AdminOperate {
    private User Admin;

    //该字段用于判断管理员是否可用
    private Boolean usalbe;

    public AdminOperate(User Admin) {
        this.Admin = Admin;
    }

    /**
     * 查询所有学生
     */
    public void selectAllStudents() throws SQLException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        System.out.println("查询所有学生");
        Dao dao = new Dao();
        ResultSet rs = dao.select(Student.class, "");
        try {
            User user = null;
            Student student = null;
            while (rs.next()) {
                user= ResultSetToObject.resultSetToObject(rs, User.class);
                user.toString();
                student = ResultSetToObject.resultSetToObject(rs, Student.class);
                student.toString();
            }
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        //输入E退出
        System.out.println("键入E退出");
        String exit;
        while (true) {
            exit = scanner.next();
            if ("E".equals(exit)) {
                System.out.println("===== 管理员菜单 =====");
                System.out.println("1. 查询所有学生");
                System.out.println("2. 修改学生手机号");
                System.out.println("3. 查询所有课程");
                System.out.println("4. 修改课程学分");
                System.out.println("5. 查询某课程的学生名单");
                System.out.println("6. 查询某学生的选课情况");
                System.out.println("7. 退出");
                System.out.println("请选择操作（输入 1-7）：");
                return;
            }
        }
    }

    /**
     * 修改学生手机号
     */
    public void updateStudentPhone() throws IllegalAccessException, SQLException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        System.out.println("键入E退出当前输入");
        //获取输入的原手机号和新手机号
        System.out.println("\n输入原手机号：");
        String oldPhone = scanner.next();
        System.out.println("输入密码：");
        String password = scanner.next();
        //验证密码是否正确
        User user = null;
        Dao dao = new Dao();

        user = dao.selectClass(User.class, "where user_email = '" + oldPhone + "'" + ", password = '" + password + "'");
        if (user == null) {
            System.out.println("账号或密码错误！");
        } else {
            System.out.println("请输入新手机号：");
            String newPhone = scanner.nextLine();
            //更新数据库
            user.setEmail(newPhone);
            int result = dao.change(user);
            if (result == 1) {
                System.out.println("修改成功！");
            } else {
                System.out.println("修改失败！");
            }
        }

        //输入E退出
        System.out.println("键入E退出");
        String exit;
        while (true) {
            exit = scanner.next();
            if (exit.equals("E")) {
                System.out.println("===== 管理员菜单 =====");
                System.out.println("1. 查询所有学生");
                System.out.println("2. 修改学生手机号");
                System.out.println("3. 查询所有课程");
                System.out.println("4. 修改课程学分");
                System.out.println("5. 查询某课程的学生名单");
                System.out.println("6. 查询某学生的选课情况");
                System.out.println("7. 退出");
                System.out.println("请选择操作（输入 1-7）：");
                return;
            }
        }
    }

    /**
     * 查询所有课程
     */
    public void selectAllCourses() throws SQLException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        //查询所有课程
        Dao dao = new Dao();
        Course course = null;
        try {
            ResultSet rs = dao.selectClass(Course.class, "");
            if(rs==null){
                System.out.println("没有查询到相关课程");
            }
            while (rs.next()) {
                course = ResultSetToObject.resultSetToObject(rs, Course.class);
                System.out.println(course.toString());
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        //输入E退出
        System.out.println("键入E退出");
        String exit;
        while (true) {
            exit = scanner.next();
            if (exit.equals("E")) {
                System.out.println("===== 管理员菜单 =====");
                System.out.println("1. 查询所有学生");
                System.out.println("2. 修改学生手机号");
                System.out.println("3. 查询所有课程");
                System.out.println("4. 修改课程学分");
                System.out.println("5. 查询某课程的学生名单");
                System.out.println("6. 查询某学生的选课情况");
                System.out.println("7. 退出");
                System.out.println("请选择操作（输入 1-7）：");
                return;
            }
        }
    }

    /**
     * 修改课程学分
     */
    public void updateCourseCredit() throws IllegalAccessException, SQLException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        System.out.println("请输入要修改的课程：");
        String courseName = scanner.nextLine();
        Dao dao = new Dao();
        ResultSet re = dao.select(Course.class,"course_name = '"+courseName+"'");
        Course course = null;
        if(re.next()) {
            course= ResultSetToObject.resultSetToObject(re, Course.class);
            System.out.println("当前课程分数：");
            System.out.println(course.getScore());
            System.out.println("请输入修改后的分数：");
            int score = scanner.nextInt();
            course.setScore(score);
            dao.change(course);
            while(re.next()){
                course= ResultSetToObject.resultSetToObject(re, Course.class);
                course.setScore(score);
                dao.change(course);
            }
        }else{
            System.out.println("未找到该课程");
        }
        //输入E退出
        System.out.println("键入E退出");
        String exit;
        while (true) {
            exit = scanner.next();
            if (exit.equals("E")) {
                System.out.println("===== 管理员菜单 =====");
                System.out.println("1. 查询所有学生");
                System.out.println("2. 修改学生手机号");
                System.out.println("3. 查询所有课程");
                System.out.println("4. 修改课程学分");
                System.out.println("5. 查询某课程的学生名单");
                System.out.println("6. 查询某学生的选课情况");
                System.out.println("7. 退出");
                System.out.println("请选择操作（输入 1-7）：");
                return;
            }
        }
    }

    /**
     * 查询某课程的学生名单
     */
    public void selectCourseStudents() throws SQLException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        System.out.println("请输入要查询的课程名称：");
        String courseName = scanner.nextLine();
        Dao dao = new Dao();
        ResultSet rs =dao.select(StudentCourses.class, "course_name = '"+courseName+"'");
        if(rs.next()) {
            System.out.println("查询结果如下：");
            StudentCourses studentCourses = null;
            studentCourses= ResultSetToObject.resultSetToObject(rs, StudentCourses.class);
            System.out.println(studentCourses.toString());
            while (rs.next()){
                studentCourses= ResultSetToObject.resultSetToObject(rs, StudentCourses.class);
                System.out.println(studentCourses.toString());
            }
        }else{
            System.out.println("没有查询到相关课程");
        }
        //输入E退出
        System.out.println("键入E退出");
        String exit;
        while (true) {
            exit = scanner.next();
            if (exit.equals("E")) {
                System.out.println("===== 管理员菜单 =====");
                System.out.println("1. 查询所有学生");
                System.out.println("2. 修改学生手机号");
                System.out.println("3. 查询所有课程");
                System.out.println("4. 修改课程学分");
                System.out.println("5. 查询某课程的学生名单");
                System.out.println("6. 查询某学生的选课情况");
                System.out.println("7. 退出");
                System.out.println("请选择操作（输入 1-7）：");
                return;
            }
        }
    }

    /**
     * 查询某学生的选课情况
     */
    public void selectStudentCourses() throws SQLException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        System.out.println("请输入学号 ");
        String studentId = scanner.nextLine();
        //找寻相关学生
        Dao dao = new Dao();
        ResultSet rs = dao.selectClass(StudentCourses.class,"student_num = '"+studentId+"'");
        if(rs.next()){
            StudentCourses studentCourses = null;
            studentCourses = ResultSetToObject.resultSetToObject(rs, StudentCourses.class);
            System.out.println(studentCourses.toString());
            while(rs.next()){
                studentCourses = ResultSetToObject.resultSetToObject(rs, StudentCourses.class);
                System.out.println(studentCourses.toString());
            }
        }else {
            System.out.println("该学生未选课");
        }
        //输入E退出
        System.out.println("键入E退出");
        String exit;
        while (true) {
            exit = scanner.next();
            if ("E".equals(exit)) {
                System.out.println("===== 管理员菜单 =====");
                System.out.println("1. 查询所有学生");
                System.out.println("2. 修改学生手机号");
                System.out.println("3. 查询所有课程");
                System.out.println("4. 修改课程学分");
                System.out.println("5. 查询某课程的学生名单");
                System.out.println("6. 查询某学生的选课情况");
                System.out.println("7. 退出");
                System.out.println("请选择操作（输入 1-7）：");
                return;
            }
        }
    }



    public void operator() throws IllegalAccessException, SQLException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        System.out.println("===== 管理员菜单 =====");
        System.out.println("1. 查询所有学生");
        System.out.println("2. 修改学生手机号");
        System.out.println("3. 查询所有课程");
        System.out.println("4. 修改课程学分");
        System.out.println("5. 查询某课程的学生名单");
        System.out.println("6. 查询某学生的选课情况");
        System.out.println("7. 退出");
        System.out.println("请选择操作（输入 1-7）：");


        while (true) {

            String choice;
            choice = scanner.nextLine();
            scanner.nextLine();
            if (choice.equals("1")) {
                //查询所有学生
                selectAllStudents();
            } else if (choice.equals("2")) {
                //修改学生手机号
                updateStudentPhone();
                } else if (choice.equals("3")) {
                //查询所有课程
                selectAllCourses();
            } else if (choice.equals("4")) {
                //修改课程学分
                updateCourseCredit();
            } else if (choice.equals("5")) {
                //查询某课程的学生名单
                selectCourseStudents();
            } else if (choice.equals("6")) {
                //查询某学生的选课情况
                selectStudentCourses();
            } else if (choice.equals("7")) {
                System.out.println("退出");
                System.out.println("=======================================");
                System.out.println("学生选课管理系统");
                System.out.println("==========");
                System.out.println("1. 登录");
                System.out.println("2. 注册");
                System.out.println("3. 退出");
                System.out.println("请选择操作（输入 1-3）：");
                return;
            } else {
                System.out.println("输入错误，请重新输入！");
            }
        }
    }
}
