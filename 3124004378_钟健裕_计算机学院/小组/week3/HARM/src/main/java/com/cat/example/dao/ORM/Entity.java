package com.cat.example.dao.ORM;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这是Entity的注解类，用于标识一个类为数据库实体类。
 * @author 钟健裕
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
    // 数据库表名
    String tableName();
    String type() default "";
}
