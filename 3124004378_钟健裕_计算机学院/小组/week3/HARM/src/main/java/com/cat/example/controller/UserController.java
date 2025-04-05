package com.cat.example.controller;

import com.cat.example.dao.Dao;
import com.cat.example.entity.User;
import com.cat.example.service.UserService;
import com.cat.example.util.JsonUtil;
import com.cat.example.util.MyBaseServlet.Action;
import com.cat.example.util.MyBaseServlet.BaseServlet;
import com.fasterxml.jackson.databind.ObjectMapper;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 *用于处理用户相关的请求
 * @author 钟健裕
 */
@WebServlet(name = "UserController", urlPatterns = "/api/user/*")
public class UserController extends BaseServlet {

    @Action("login")
    public void loginController(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService.login(request, response);
    }

    @Action("register")
    public void registerController(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException {
        UserService.register(request, response);
    }

    @Action("attest")
    public void attestController(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException {
        UserService.attest(request, response);
    }
}
