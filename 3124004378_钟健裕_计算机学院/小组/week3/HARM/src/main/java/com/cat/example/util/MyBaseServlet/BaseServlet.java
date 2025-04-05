package com.cat.example.util.MyBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 钟健裕
 *  BaseServlet 基类
 *  继承 HttpServlet 类，并实现 service 方法，该方法用于处理请求参数中的 action 参数，并调用相应的 @Action 方法
 *  该类提供了 findActionMethod 方法，用于通过反射查找带有 @Action 注解的方法
 */
public abstract class BaseServlet extends HttpServlet {

    @Override
    protected void service(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        // 设置请求编码
        req.setCharacterEncoding("UTF-8");
        // 设置 CORS 头（适用于所有请求）
        resp.setHeader("Access-Control-Allow-Origin", "*");
        // 允许请求方法
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        // 允许 Content-Type 头
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        // 缓存预检结果 24 小时
        resp.setHeader("Access-Control-Max-Age", "86400");
        // 设置响应内容类型
        resp.setContentType("application/json");

        // 单独处理 OPTIONS 请求
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            System.out.println("收到预检请求");
            resp.setStatus(HttpServletResponse.SC_OK);
            return; // 直接返回，不执行后续逻辑
        }
        // 处理实际请求
        String pathInfo = req.getPathInfo();
        if(pathInfo == null){
            resp.sendError(404);
            return;
        }

        try {
            // 2. 查找匹配的 @Action 方法
            Method method = findActionMethod(pathInfo);
            if (method == null) {
                resp.sendError(404, "找不到 Action 方法: " + pathInfo);
                return;
            }

            // 3. 调用目标方法
            method.invoke(this, req, resp);

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ServletException("无法调用 Action 方法", e);
        }
    }

    // 通过反射查找带有 @Action 注解的方法
    private Method findActionMethod(String action) {
        for (Method method : this.getClass().getDeclaredMethods()) {
            Action annotation = method.getAnnotation(Action.class);
            if (annotation != null && annotation.value().equals(action)) {
                // 找到带有 @Action 注解的方法，检查参数类型是否正确
                Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes.length == 2 &&
                        paramTypes[0] == HttpServletRequest.class &&
                        paramTypes[1] == HttpServletResponse.class) {
                    return method;
                }
            }
        }
        return null;
    }
}