package com.cat.example.util.MyBaseServlet;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 钟健裕
 * 用于标记方法对应的的api
 */
@Retention(RetentionPolicy.RUNTIME) // 运行时保留
@Target(ElementType.METHOD)         // 只能标记方法
public @interface Action {
    // 动作标识
    String value();
}