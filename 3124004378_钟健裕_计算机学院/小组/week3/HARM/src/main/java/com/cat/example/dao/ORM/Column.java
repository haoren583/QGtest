package com.cat.example.dao.ORM;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 映射字段名与数据库列名
 * @author 钟健裕
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    //映射的数据库列名
   String name();
   //别名
   String alias() default "";
}
