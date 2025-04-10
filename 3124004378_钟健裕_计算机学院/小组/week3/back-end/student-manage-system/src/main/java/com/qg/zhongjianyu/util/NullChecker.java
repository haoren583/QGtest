package com.qg.zhongjianyu.util;

import java.lang.reflect.Field;

/**
 * @author 钟健裕
 * 用于检查对象的字段是否有null值
 */
public class NullChecker {
    /**
     * 检查对象是否有null值
     * @param obj 要检查的对象
     * @return true：有null值；false：没有null值
     */
    public static boolean hasNull(Object obj) throws IllegalAccessException {
        // 获取对象的类
        Class<?> clazz = obj.getClass();
        // 获取所有声明的字段
        Field[] fields = clazz.getDeclaredFields();
        // 遍历所有字段
        for (Field field : fields) {
            // 设置字段可访问（即使是私有的）
            field.setAccessible(true);
            // 获取字段的值
            Object value = field.get(obj);
            // 检查字段值是否为null
            if (value == null) {
                return true;
            }
        }
        return false;
    }
}
