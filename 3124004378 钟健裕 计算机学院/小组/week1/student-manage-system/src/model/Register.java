package model;

import DataBaseConnectPool.Dao;

import java.util.logging.Logger;

import static model.MyScanner.scanner;

/**
 * 注册机，用于注册新用户
 * 只有管理员才能注册新用户
 * @author 钟健裕
 */
public class Register {
    private static final Logger log = Logger.getLogger(Register.class.getName());
    // 操作员，即管理员
    private User operator;

    /**
     * 构造方法，传入操作员
     * @param operator 操作员
     */
    public Register(User operator) {
        this.operator = operator;
    }

    /**
     * 无参构造方法
     */
    public Register() {}


    public boolean register(User user) throws IllegalAccessException {
        if(/*operator.getRoleType()=="ADMIN"*/true){
            Dao dao=new Dao();
            //检查用户是否已存在
            if(dao.selectClass(User.class,"username='"+user.getUsername()+"'"+" and email='"+user.getEmail()+"'")==null){
                //注册用户
                if(dao.add(user)>0) {
                    user=dao.selectClass(User.class,"username='"+user.getUsername()+"'"+" and email='"+user.getEmail()+"' and is_del=0");
                    if(user.getRoleType().equals("STUDENT")){
                        //如果是学生，则输入学号
                        System.out.println("请输入学号：");
                        String studentNum=scanner.nextLine();
                        Student student=new Student();
                        student.setStudentNum(studentNum);
                        student.setUserId(user.getUserId());
                        student.setUsername(user.getUsername());
                        student.setPassword(user.getPassword());
                        student.setEmail(user.getEmail());
                        student.setSex(user.getSex());
                        student.setRoleType(user.getRoleType());
                        student.setIsDel(0);
                        student.setAmountSelectCourse(0);
                        student.setCollegeId(1);
                        student.setMajorId(1);
                        student.setGrade(1);
                        student.setClassNum(1);
                        dao.add(student);
                    }
                    log.info("注册成功！");
                    return true;
                }else{
                    log.warning("注册失败！");
                    return false;
                }
            }else{
                log.info("用户存在！");
                return false;
            }
        }else{
            log.warning("非管理员用户不能注册新用户！");
            return false;
        }
    }


}
