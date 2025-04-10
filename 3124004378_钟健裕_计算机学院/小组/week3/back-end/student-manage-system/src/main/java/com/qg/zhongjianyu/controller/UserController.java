package com.qg.zhongjianyu.controller;

import com.qg.zhongjianyu.controller.MyBaseServlet.Action;
import com.qg.zhongjianyu.controller.MyBaseServlet.BaseServlet;
import com.qg.zhongjianyu.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserController", urlPatterns = "/api/user/*")
public class UserController extends BaseServlet {

    @Action("/login")
    public void loginController(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService.login(request, response);
    }

    @Action("/register")
    public void registerController(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException {
        UserService.register(request, response);
    }

    @Action("/query_courses")
    public void queryCourseController(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService.queryCourse(request, response);
    }

}
