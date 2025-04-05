package com.cat.example.service;

import com.cat.example.constant.StatusCode;
import com.cat.example.dao.Dao;
import com.cat.example.entity.Department;
import com.cat.example.entity.Doctor;
import com.cat.example.entity.User;
import com.cat.example.util.JsonUtil;
import com.cat.example.util.Result;
import com.cat.example.util.Token;
import com.cat.example.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 钟健裕
 */
public class UserService {
    private static class LoginRequest {
        private String phone;
        private String password;

        public LoginRequest() {
        }

        public LoginRequest(String phone, String password) {
            this.phone = phone;
            this.password = password;
        }

        /**
         * 获取
         * @return phone
         */
        public String getPhone() {
            return phone;
        }

        /**
         * 设置
         * @param phone
         */
        public void setPhone(String phone) {
            this.phone = phone;
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

        @Override
        public String toString() {
            return "LoginRequest{phone = " + phone + ", password = " + password + "}";
        }
    }

    private static class RegisterRequest {
        public String phone;
        public String password;

        public RegisterRequest() {
        }
    }

    private static class AttestRequest {
        public Token token;
        public String name;
        public String role;
        public String cardId;
        public String gender;
    }

    public static void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        //解析json数据
        LoginRequest loginRequest = JsonUtil.OBJECT_MAPPER.readValue(request.getReader(), UserService.LoginRequest.class);
        //获取数据库连接
        Dao dao = new Dao();
        //查询用户信息
        User user = dao.selectClass(User.class, "phone = '" + loginRequest.phone + "' and password = '" + loginRequest.password + "'");
        Result<Map<String, Object>> result;
        //判断用户是否存在
        if (user == null) {
            result = Result.error(Result.ResultCode.NOT_FOUND);
            response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
            return;
        }
        //判断用户是否被删除
        if (user.getIsDel() == 1) {
            result = Result.error(Result.ResultCode.NOT_FOUND);
            response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
            return;
        }
        Token token = new Token(user);
        Map<String, Object> tokenMap = new HashMap<>();
        token.putToken(tokenMap);
        result = Result.success(tokenMap);
        response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
    }


    public static void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        //解析json数据
        RegisterRequest registerRequest = JsonUtil.OBJECT_MAPPER.readValue(request.getReader(), UserService.RegisterRequest.class);
        //检查用户电话号码是否合规
        if (!registerRequest.phone.matches("^1[34578]\\d{9}$")) {
            Result<Map<String, Object>> result = Result.error(Result.ResultCode.BAD_REQUEST);
            response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
            return;
        }
        if (registerRequest.password.length() < 6) {
            Result<Map<String, Object>> result = Result.error(Result.ResultCode.BAD_REQUEST);
            response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
            return;
        }

        Result<Map<String, Object>> result;
        //获取数据库连接
        Dao dao = new Dao();
        //查询用户信息
        User user = dao.selectClass(User.class, "phone = '" + registerRequest.phone + "'");
        if (user != null) {
            //判断用户是否被删除
            if (user.getIsDel() == 1) {
                user.setIsDel((byte) 0);
                dao.change(user);
                Token token = new Token(user);
                Map<String, Object> tokenMap = new HashMap<>();
                result = Result.success(tokenMap);
                response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
                return;
            }
            result = Result.error(Result.ResultCode.CONFLICT);
            response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
            return;
        }
        //创建用户
        user = new User();
        user.setPhone(registerRequest.phone);
        user.setPassword(registerRequest.password);
        user.setRole("Common");
        user.setUserId(UUID.generateUUID());
        //添加用户
        dao.add(user);
        Token token = new Token(user);
        Map<String, Object> tokenMap = new HashMap<>();
        token.putToken(tokenMap);
        result = Result.success(tokenMap);
        response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
        return;
    }

    public static void attest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException {
        Result<Map<String, Object>> result;
        //解析json数据
        HashMap<String, Object> attestRequest = JsonUtil.OBJECT_MAPPER.readValue(request.getReader(), HashMap.class);
        //获取并验证token
        Token token = Token.getToken(request.getHeader("Authorization"));
        if (token.getTokenCode() == Token.Code.INVALID) {
            result = Result.error(Result.ResultCode.UNAUTHORIZED);
            response.setStatus(result.getCode());
            response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
            return;
        }
        //获取数据库连接
        Dao dao = new Dao();
        //查询用户信息
        User user = dao.selectClass(User.class, "user_id = " + token.getPayload().get("userId"));
        if (user == null|| user.getIsDel() == 1) {
            result = Result.error(Result.ResultCode.NOT_FOUND);
            response.setStatus(result.getCode());
            response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
            return;
        }
        //更新用户信息
        user.setUserName(attestRequest.get("name").toString());
        user.setRole((attestRequest.get("role").toString()));
        user.setCardId(attestRequest.get("cardId").toString());
        user.setGender(attestRequest.get("gender").toString());
        if ("Doctor".equals(user.getRole())){
            user.setStatus(StatusCode.Doctor, true);
        }
        //更新用户信息
        dao.change(user);
        // 如果是医生，则创建医生信息
        if (Objects.equals(user.getRole(), "Doctor")) {
            //判断信息是否完整
            if (attestRequest.get("dept_name") == null || attestRequest.get("job_intro") == null || attestRequest.get("title") == null) {
                result = Result.error(Result.ResultCode.BAD_REQUEST);
                response.setStatus(result.getCode());
                response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
                return;
            }

            Doctor doctor = new Doctor();
            //判断科室是否存在
            String deptName = attestRequest.get("dept_name").toString();
            Department department = dao.selectClass(Department.class, "dept_name = '" + deptName + "'");
            if (department == null) {
                result = Result.error(Result.ResultCode.NOT_FOUND);
                response.setStatus(result.getCode());
                response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
                return;
            }
            //创建医生信息
            doctor.setDoctorName(attestRequest.get("name").toString());
            doctor.setDeptId(department.getDeptId());
            doctor.setIntro(attestRequest.get("job_intro").toString());
            doctor.setTitle(attestRequest.get("title").toString());
            doctor.setSchedule(attestRequest.get("schedule").toString());
            doctor.setUserId(user.getUserId());
            doctor.setStatus(user);
            //判断认证信息是否存在
            if(dao.selectClass(Doctor.class,"doctor_id = "+doctor.getDoctorId())){
                //更新医生信息
                dao.change(doctor);
            }else {
                //添加医生信息
                dao.add(doctor);
            }
        }

        //返回结果
        Map<String, Object> resultMap = new HashMap<>();
        token.putToken(resultMap);
        result = Result.success(resultMap);
        response.setStatus(result.getCode());
        response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
        return;
    }
}
