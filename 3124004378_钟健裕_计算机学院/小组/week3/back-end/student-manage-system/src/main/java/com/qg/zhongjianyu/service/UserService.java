package com.qg.zhongjianyu.service;

import com.qg.zhongjianyu.dao.Dao;
import com.qg.zhongjianyu.entity.Course;
import com.qg.zhongjianyu.entity.Student;
import com.qg.zhongjianyu.entity.User;
import com.qg.zhongjianyu.util.JsonUtil;
import com.qg.zhongjianyu.util.Result;
import com.qg.zhongjianyu.util.Token;
import com.qg.zhongjianyu.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    public static void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("登录请求");
        Result<Map<String, Object>> result;
        //解析json数据
        try {
            HashMap<String, Object> loginRequest = JsonUtil.OBJECT_MAPPER.readValue(request.getReader(), HashMap.class);

            //需要的参数
            String phone = loginRequest.get("phone").toString();
            String password = loginRequest.get("password").toString();

            if (phone == null || password == null){
                result = Result.error(Result.ResultCode.UNAUTHORIZED);
                result.putIntoResponse(response);
                return;
            }

            //查询数据库
            Dao dao = new Dao();
            User user = dao.selectClass(User.class, "phone = '" + phone + "' AND password = '" + password + "'");
            if (user == null){
                result = Result.error(Result.ResultCode.UNAUTHORIZED);
                result.putIntoResponse(response);
                return;
            }

            //生成token
            Token token = new Token(user);
            Map<String, Object> tokenMap = new HashMap<>();
            token.putToken(tokenMap);
            result = Result.success(tokenMap);
            result.putIntoResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error(Result.ResultCode.BAD_REQUEST);
            result.putIntoResponse(response);
        }
    }

    public static void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException {
        System.out.println("注册请求");
        Result<Map<String, Object>> result;
        //解析json数据
        try {
            HashMap<String, Object> registerRequest = JsonUtil.OBJECT_MAPPER.readValue(request.getReader(), HashMap.class);

            //需要的参数
            String password = registerRequest.get("password").toString();
            String phone = registerRequest.get("phone").toString();
            String userName = registerRequest.get("userName").toString();

            if ( password == null || phone == null || userName == null){
                result = Result.error(Result.ResultCode.BAD_REQUEST);
                result.putIntoResponse(response);
                return;
            }

            Dao dao = new Dao();
            User user = dao.selectClass(User.class, "phone = '" + phone + "'");
            if (user != null){
                result = Result.error(Result.ResultCode.CONFLICT);
                result.putIntoResponse(response);
                return;
            }
            //插入数据库
            user = new User();
            user.setPassword(password);
            user.setPhone(phone);
            user.setUserName(userName);
            user.setUserId(UUID.generateUUID());

            Student student = new Student();
            student.setUserId(user.getUserId());

            dao.add(user);
            dao.add(student);

            //生成token
            Token token = new Token(user);
            Map<String, Object> tokenMap = new HashMap<>();
            token.putToken(tokenMap);
            result = Result.success(tokenMap);
            result.putIntoResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error(Result.ResultCode.BAD_REQUEST);
            result.putIntoResponse(response);
        }
    }

    public static void queryCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("查询课程请求");
        Result<Map<String, Object>> result;
        //解析json数据
        try {
            HashMap<String, Object> queryCourseRequest = JsonUtil.OBJECT_MAPPER.readValue(request.getReader(), HashMap.class);
            //获取并验证token
            Token token = Token.getToken(request.getHeader("Authorization"));
            if (token.getTokenCode() == Token.Code.INVALID) {
                result = Result.error(Result.ResultCode.UNAUTHORIZED);
                result.putIntoResponse(response);
                return;
            }

            Dao dao = new Dao();
            //查询数据库
            List<Course> courseList = dao.selectList(Course.class,"");
            //封装返回数据
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("courseList", courseList);
            result = Result.success(resultMap);
            result.putIntoResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error(Result.ResultCode.BAD_REQUEST);
            result.putIntoResponse(response);
        }
    }
}
