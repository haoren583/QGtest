package model;

import DataBaseConnectPool.Dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

import static model.MyScanner.scanner;

/**
 * 主菜单类
 */
public class MainManu {
    public MainManu() {

    }

    /**
     * 登录
     */
    public void login() {
        System.out.println("登录");
        SignIn signIn = new SignIn();

        System.out.println("请输入电话号码：");
        String phone = scanner.nextLine();
        System.out.println("请输入密码：");
        String password = scanner.nextLine();
        User user = signIn.signIn(phone, password);
        if (user != null) {
            System.out.println("登录成功");
            if (user.getRoleType().equals("STUDENT")) {
                Dao dao = new Dao();
                Student student = dao.selectClass(Student.class, "user_id=" + user.getUserId());
                StudentOperate studentOperate = new StudentOperate(student);
                try {
                    studentOperate.operate();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                AdminOperate adminOperate = new AdminOperate(user);
                try {
                    adminOperate.operator();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("登录失败");
            System.out.println("=======================================");
            System.out.println("学生选课管理系统");
            System.out.println("=======================================");
            System.out.println("1. 登录");
            System.out.println("2. 注册");
            System.out.println("3. 退出");
            System.out.println("请选择操作（输入 1-3）：");
            return;
        }

    }

    /**
     * 注册
     */
    public void register() {
        System.out.println("注册");
        Register register=new Register();

        System.out.print("请输入姓名：");
        String name = scanner.nextLine();

        System.out.print("请输入性别：");
        int sex = 0;
        while (true) {
            String gender = scanner.nextLine();
            if (gender.equals("男")) {
                sex = 1;
                break;
            } else if (gender.equals("女")) {
                sex = 0;
                break;
            } else {
                System.out.println("性别输入错误，请重新输入");
                continue;
            }
        }
        System.out.print("请输入用户类型：输入 1 代表学生，2 代表管理员");
        String roleType = scanner.nextLine();
        String roleTypeStr = "";
        if (Objects.equals(roleType, "1")) {
        roleTypeStr = "STUDENT";
        } else if (Objects.equals(roleType, "2")) {
            roleTypeStr = "ADMIN";
        }

        System.out.print("请输入电话号码：");
        String phone = scanner.next();

        System.out.print("请输入密码：");
        String password = scanner.next();

        User user = new User(-1, name, password, phone,sex, roleTypeStr);
        //调用注册方法
        try {
            boolean seccess = register.register(user);
            if (seccess) {
                System.out.println("注册成功");
            } else {
                System.out.println("注册失败");
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        System.out.println("=======================================");
        System.out.println("学生选课管理系统");
        System.out.println("=======================================");
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.println("3. 退出");
        System.out.println("请选择操作（输入 1-3）：");
        return;
    }




    public void operate(){
        System.out.println("=======================================");
        System.out.println("学生选课管理系统");
        System.out.println("=======================================");
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.println("3. 退出");
        System.out.println("请选择操作（输入 1-3）：");

        while (true) {
            String input = scanner.nextLine();
            int choice = 0;
            if (input.matches("\\d+")) {
                choice = Integer.parseInt(input);
            } else {
                System.out.println("输入错误，请重新输入");
                continue;
            }
            switch (choice) {
                case 1:
                    //登录
                    login();
                    break;
                case 2:
                    //注册
                    register();
                    break;
                case 3:
                    System.out.println("退出");
                    return;
                default:
                    System.out.println("输入错误，请重新输入");
                    break;
            }
        }
    }
}
