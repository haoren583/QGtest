package DataBaseConnectPool.ORM;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 钟健裕
 */
public class ResultSetToObject {
    public static <T> T resultSetToObject(ResultSet rs, Class<T> entityClass) throws IllegalAccessException, SQLException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 通过反射创建实体对象
        // 创建实体对象
        T entity = entityClass.getDeclaredConstructor().newInstance();
        // 获取所有字段名
        Field[] fields = entityClass.getDeclaredFields();
        //@Column注解的字段
        List<Field> columnFields = new ArrayList<>();
        //@Column注解的字段名
        List<String> fieldNames = new ArrayList<>();
        // 遍历所有字段
        for (int i = 0; i < fields.length; i++) {
            // 判断是否有@Column注解
            if (fields[i].isAnnotationPresent(Column.class)) {
                // 设置字段可访问
                fields[i].setAccessible(true);
                // 获取字段名
                String fieldName = fields[i].getAnnotation(Column.class).name();
                columnFields.add(fields[i]);
                fieldNames.add(fieldName);
            }
        }

        //如果没有找到结果
        if (rs.next() == false) {
            return null;
        }

        // 遍历字段
        for (int i = 0; i < fieldNames.size(); i++) {
            // 获取字段值
            Object fieldValue = rs.getObject(fieldNames.get(i));
            // 设置字段值
            columnFields.get(i).set(entity, fieldValue);
        }

        return entity;
    }
}
