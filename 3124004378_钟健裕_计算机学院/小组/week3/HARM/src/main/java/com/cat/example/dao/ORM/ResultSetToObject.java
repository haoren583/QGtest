package com.cat.example.dao.ORM;

import com.cat.example.entity.IMultiTableEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ResultSetToObject 类用于将ResultSet结果集转换为实体对象
 * @author 钟健裕
 */
public class ResultSetToObject {
    /**
     * 将一个ResultSet结果集转换为一个实体对象
     * @param rs ResultSet结果集
     * @param entityClass 实体类
     * @return 实体对象
     * @throws IllegalAccessException 非法访问异常
     * @throws SQLException SQL异常
     * @throws InstantiationException 实例化异常
     * @throws NoSuchMethodException 没有方法异常
     * @throws InvocationTargetException 调用目标异常
     */
    public static <T> T resultSetToObject(ResultSet rs, Class<T> entityClass) throws IllegalAccessException, SQLException, InstantiationException, NoSuchMethodException, InvocationTargetException {


        if (rs == null) {
            return null;
        }
        // 通过反射创建实体对象
        // 创建实体对象
        T entity = entityClass.getDeclaredConstructor().newInstance();
        //判断entityClass是否来自接口“IMultiTableEntity”
        if (IMultiTableEntity.class.isAssignableFrom(entityClass)) {
            // 获取类的所有字段名
            Field[] fields = entityClass.getDeclaredFields();
            //@Column注解的字段
            List<Field> columnFields = new ArrayList<>();
            //@Column注解的字段名
            List<String> fieldNames = new ArrayList<>();
            // 遍历所有字段
            for (Field field : fields) {
                // 判断是否有@Column注解
                if (field.isAnnotationPresent(Column.class)) {
                    // 设置字段可访问
                    field.setAccessible(true);
                    // 获取字段名或别名
                    String fieldName;
                    if (field.getAnnotation(Column.class).alias().isEmpty()) {
                        fieldName = field.getAnnotation(Column.class).name();
                    }else{
                        fieldName = field.getAnnotation(Column.class).alias();
                    }
                    // 添加到列表中
                    columnFields.add(field);
                    fieldNames.add(fieldName);
                }
            }

            //如果没有找到结果
            if (!rs.next()) {
                return null;
            }

            // 遍历字段
            for (int i = 0; i < fieldNames.size(); i++) {
                // 获取字段值
                Object fieldValue = rs.getObject(fieldNames.get(i));
                // 设置字段值
                //如果是枚举类型，需要转换为枚举类型
                if (fieldValue instanceof String && columnFields.get(i).getType().isEnum()) {
                    // 首先获取字段的类型
                    Class<?> fieldType = columnFields.get(i).getType();
                    // 然后获取枚举类型
                    fieldValue = Enum.valueOf((Class<Enum>) columnFields.get(i).getType(), (String) fieldValue);
                }else if(fieldValue instanceof Integer && columnFields.get(i).getType() == byte.class){
                    //如果是byte类型，需要转换为byte类型
                    fieldValue = ((Integer) fieldValue).byteValue();
                }
                columnFields.get(i).set(entity, fieldValue);
            }
            return entity;

        }
        // 获取所有字段名
        Field[] fields = entityClass.getDeclaredFields();
        //@Column注解的字段
        List<Field> columnFields = new ArrayList<>();
        //@Column注解的字段名
        List<String> fieldNames = new ArrayList<>();
        // 遍历所有字段
        for (Field field : fields) {
            // 判断是否有@Column注解
            if (field.isAnnotationPresent(Column.class)) {
                // 设置字段可访问
                field.setAccessible(true);
                // 获取字段名
                String fieldName = field.getAnnotation(Column.class).name();
                columnFields.add(field);
                fieldNames.add(fieldName);
            }
        }

        //如果没有找到结果
        if (!rs.next()) {
            return null;
        }

        // 遍历字段
        for (int i = 0; i < fieldNames.size(); i++) {
            // 获取字段值
            Object fieldValue = rs.getObject(fieldNames.get(i));
            // 设置字段值
            //如果是枚举类型，需要转换为枚举类型
            if (fieldValue instanceof String && columnFields.get(i).getType().isEnum()) {
                // 首先获取字段的类型
                Class<?> fieldType = columnFields.get(i).getType();
                // 然后获取枚举类型
                fieldValue = Enum.valueOf((Class<Enum>) columnFields.get(i).getType(), (String) fieldValue);
            }else if(fieldValue instanceof Integer && columnFields.get(i).getType() == byte.class){
                //如果是byte类型，需要转换为byte类型
                fieldValue = ((Integer) fieldValue).byteValue();
            }
            columnFields.get(i).set(entity, fieldValue);
        }

        return entity;
    }

    /**
     * 将一个ResultSet结果集转换为一个实体对象的列表
     * @param rs ResultSet结果集
     * @param entityClass 实体类
     * @return 实体对象的列表
     * @throws IllegalAccessException 非法访问异常
     * @throws SQLException SQL异常
     * @throws InstantiationException 实例化异常
     * @throws NoSuchMethodException 没有方法异常
     * @throws InvocationTargetException 调用目标异常
     */
    public static <T> List<T> resultSetToList(ResultSet rs, Class<T> entityClass) throws IllegalAccessException, SQLException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            T entity = resultSetToObject(rs, entityClass);
            list.add(entity);
        }
        return list;
    }
}
