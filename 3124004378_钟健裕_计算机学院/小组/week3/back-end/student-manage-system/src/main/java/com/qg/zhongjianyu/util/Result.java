package com.qg.zhongjianyu.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于封装返回结果的类
 * @author 钟健裕
 */
public class Result<T extends Map> {

    public enum ResultCode {
        // 成功状态码
        SUCCESS(200, "操作成功"),

        // 客户端错误
        BAD_REQUEST(400, "参数错误"),
        UNAUTHORIZED(401, "未授权"),
        FORBIDDEN(403, "禁止访问"),
        NOT_FOUND(404, "资源不存在"),
        CONFLICT(409, "资源冲突"),
        TOKEN_EXPIRED(419, "token失效"),

        // 服务端错误
        INTERNAL_ERROR(500, "服务器内部错误"),
        SERVICE_UNAVAILABLE(503, "服务不可用");

        // 状态码
        private final int code;
        // 提示信息
        private final String message;

        // 构造方法
        ResultCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        // Getter 方法
        public int getCode() { return code; }
        public String getMessage() { return message; }
    }

    // 状态码
    private int code;
    // 状态信息
    private String msg;
    // 返回数据
    private T data;


    // 私有构造方法
    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 静态工厂方法（成功）
    public static <T extends Map> Result<T> success(T data) {
        return new Result<>(
                ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMessage(),
                data
        );
    }

    //静态工厂方法（错误）
    public static <T extends Map> Result<T> error(ResultCode resultCode) {
        return new Result<>(
                resultCode.getCode(),
                resultCode.getMessage(),
                null
        );
    }

    // 实例方法
    public Result<T> msg(String newMsg) {
        this.msg = newMsg;
        return this;
    }

    /**
     * 将result对象塞入response中
     * @param response
     */
    public void putIntoResponse(HttpServletResponse response) throws IOException {
        response.setStatus(code);
        if (data == null){
            Map<String, Object> data = new HashMap<>();
            data.put("code", code);
            data.put("msg", msg);
            response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(data));
            return;
        }
        data.put("code", code);
        data.put("msg", msg);
        response.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(data));
    }

    // Getter 方法
    public int getCode() { return code; }
    public String getMsg() { return msg; }
    public T getData() { return data; }

}
