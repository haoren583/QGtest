package DataBaseConnectPool.ORM;

import org.junit.Test;

import javax.xml.crypto.Data;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL语句生成器
 */
public class SQLGenerator {
    /**
     * 生成查询语句
     * @param entityClass 实体类
     * @param columns 列名数组
     * @param where 条件
     * @return 查询语句
     */
    public static String generateSelectSQL(Class<?> entityClass, String[] columns, String where) {
        String tableName = getTableName(entityClass);
        // 构建SELECT语句
        StringBuilder sql = new StringBuilder("SELECT ");
        for (int i = 0; i < columns.length; i++) {
            sql.append(columns[i]);
            if (i < columns.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(" FROM ").append(tableName);
        if (where!= null &&!where.isEmpty()) {
            sql.append(" WHERE ").append(where);
        }
        return sql.toString();
    }

    /**
     * 生成查询一行全部内容的语句
     * @param entityClass 实体类
     * @param where 条件
     * @return 查询语句
     */
    public static String generateSelectSQL(Class<?> entityClass, String where) {
        return generateSelectSQL(entityClass, new String[] {"*"}, where);
    }

    /**
     * 获取表名
     * @param entityClass 实体类
     * @return 表名
     */
    public static String getTableName(Class<?> entityClass) {
        //从传入的entityClass中获取DataEntity注解
        DataEntity annotation = entityClass.getAnnotation(DataEntity.class);
        //如果注解为null，则返回类名
        return annotation.tableName().isEmpty() ? entityClass.getSimpleName() : annotation.tableName();
    }



    /**
     * 生成插入语句
     * @param entity 实体对象
     * @return 插入语句
     */
    public static String generateInsertSQL(Object entity) throws IllegalAccessException {
        // 获取实体类
        Class<?> entityClass = entity.getClass();
        // 获取表名
        String tableName = getTableName(entityClass);
        // 构建INSERT语句
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        // 获取所有字段名
        Field[] fields = entityClass.getDeclaredFields();
        //具有@Column注解的字段
        List<Field> columnFields = new ArrayList<>();
        //字段名
        List<String> fieldNames = new ArrayList<>();
        // 遍历所有字段
        for (int i = 0; i < fields.length; i++) {
            //判断是否有@AutoPlusId注解
            if (fields[i].isAnnotationPresent(AutoPlusId.class)) {
                //跳过自增主键
                continue;
            }
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
        // 构建INSERT语句
        for (int i = 0; i < fieldNames.size(); i++) {
            sql.append(fieldNames.get(i));
            if (i < fieldNames.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(") VALUES (");
//        // 构建VALUES语句，使用占位符
//        for (int i = 0; i < columnFields.size(); i++) {
//            sql.append("?");
//            if (i < columnFields.size() - 1) {
//                sql.append(", ");
//            }
//        }
//        sql.append(")");
        // 构建VALUES语句
        for (int i = 0; i < columnFields.size(); i++) {
            // 获取字段值
            Object fieldValue = columnFields.get(i).get(entity);
            //如果字段值为字符串，需要加单引号
            if (fieldValue instanceof String) {
                sql.append("'").append(fieldValue).append("'");
            } else {
                //构建VALUES语句
                sql.append(fieldValue);
            }
            if (i < columnFields.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");
        return sql.toString();
    }

    /**
     * 生成行覆盖语句，覆盖原有行
     * @param entity 实体对象
     * @return 更新语句
     */
    public static String generateUpdateSQL(Object entity) throws IllegalAccessException {
        // 获取实体类
        Class<?> entityClass = entity.getClass();
        // 获取表名
        String tableName = getTableName(entityClass);
        // 构建UPDATE语句
        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        // 获取所有字段名
        Field[] fields = entityClass.getDeclaredFields();
        //具有@Column注解的字段
        List<Field> columnFields = new ArrayList<>();
        //字段名
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
        // 构建SET语句
        for (int j = 0; j < fieldNames.size(); j++) {
            // 获取字段值
            Object fieldValue = columnFields.get(j).get(entity);
            //如果字段值为字符串，需要加单引号
            if (fieldValue instanceof String) {
                sql.append(fieldNames.get(j)).append(" = '").append(fieldValue).append("'");
            } else {
                //构建SET语句
                sql.append(fieldNames.get(j)).append(" = ").append(fieldValue);
            }
            if (j < fieldNames.size() - 1) {
                sql.append(", ");
            }
        }
        //寻找主键字段
        String primaryKeyFieldName = null;
        String primaryKeyValue = null;
        for (int i = 0; i < fieldNames.size(); i++) {
            // 判断是否有@Id注解
            if (fields[i].isAnnotationPresent(Id.class)) {
                //获取主键字段名
                primaryKeyFieldName = fieldNames.get(i);
                //获取主键字段值
                //如果字段值为字符串，需要加单引号
                Object fieldValue = columnFields.get(i).get(entity);
                if (fieldValue instanceof String) {
                    primaryKeyValue = "'" + fieldValue + "'";
                } else {
                    primaryKeyValue = fieldValue.toString();
                }
                break;
            }
        }
        // 如果没有找到主键字段，则抛出异常
        if(primaryKeyFieldName == null) {
            throw new IllegalArgumentException("未找到主键字段");
        }

        // 构建WHERE语句
        String where = primaryKeyFieldName + " = " + primaryKeyValue;
        sql.append(" WHERE ").append(where);
        return sql.toString();
    }
}
