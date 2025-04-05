package com.cat.example.dao.ORM;

import com.cat.example.entity.IMultiTableEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL语句生成器
 * @author 钟健裕
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
        Entity annotation = entityClass.getAnnotation(Entity.class);
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
        //具有@Column注解的，且无@DataAuto注解的字段
        List<Field> columnFields = new ArrayList<>();
        //字段名
        List<String> fieldNames = new ArrayList<>();
        //字段值
        List<Object> fieldValues = new ArrayList<>();
        // 遍历所有字段
        for (int i = 0; i < fields.length; i++) {
            //判断是否有@DataAuto注解
            if (fields[i].isAnnotationPresent(DataAuto.class)) {
                continue;
            }
            // 判断是否有@Column注解
            if (fields[i].isAnnotationPresent(Column.class)) {
                // 设置字段可访问
                fields[i].setAccessible(true);
                // 获取字段值
                Object fieldValue = fields[i].get(entity);
                //如果字段值位null，则不添加到SQL语句中
                if (fieldValue == null) {
                    continue;
                }
                fieldValues.add(fieldValue);
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

        // 构建VALUES语句
        for (int i = 0; i < columnFields.size(); i++) {
            // 获取字段值
            Object fieldValue = fieldValues.get(i);
            //如果字段值为字符串或着enum类型，需要加单引号
            if (fieldValue instanceof String || fieldValue instanceof Enum) {
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
        for (Field field : fields) {
            // 判断是否有@Column注解
            //判断是否有@DataAuto注解
            if (field.isAnnotationPresent(Column.class)&&(!field.isAnnotationPresent(DataAuto.class))) {
                // 设置字段可访问
                field.setAccessible(true);
                // 获取字段名
                String fieldName = field.getAnnotation(Column.class).name();
                columnFields.add(field);
                fieldNames.add(fieldName);
            }
        }
        // 构建SET语句
        for (int j = 0; j < fieldNames.size(); j++) {
            // 获取字段值
            Object fieldValue = columnFields.get(j).get(entity);
            //如果字段值为字符串或enum类型，需要加单引号
            if (fieldValue instanceof String || fieldValue instanceof Enum) {
                sql.append(fieldNames.get(j)).append(" = '").append(fieldValue).append("'");
            } else {
                //构建SET语句
                sql.append(fieldNames.get(j)).append(" = ").append(fieldValue);
            }
            if (j < fieldNames.size() - 1) {
                sql.append(", ");
            }
        }
        // 寻找主键字段，假设有多个主键字段
        List<String> primaryKeyFieldNames = new ArrayList<>();
        List<String> primaryKeyValues = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            // 判断是否有@Id注解
            if (fields[i].isAnnotationPresent(Id.class)) {
                // 获取主键字段名
                primaryKeyFieldNames.add(fieldNames.get(i));
                try {
                    // 获取主键字段值
                    Object fieldValue = fields[i].get(entity);
                    //如果字段值为字符串，需要加单引号
                    if (fieldValue instanceof String) {
                        primaryKeyValues.add("'" + fieldValue + "'");
                    } else {
                        primaryKeyValues.add(fieldValue.toString());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("无法访问字段: " + fieldNames.get(i), e);
                }
            }
        }

        // 如果没有找到主键字段，则抛出异常
        if (primaryKeyFieldNames.isEmpty()) {
            throw new IllegalArgumentException("未找到主键字段");
        }

        // 构建WHERE语句
        StringBuilder whereClause = new StringBuilder();
        for (int i = 0; i < primaryKeyFieldNames.size(); i++) {
            whereClause.append(primaryKeyFieldNames.get(i)).append(" = ").append(primaryKeyValues.get(i));
            if (i < primaryKeyFieldNames.size() - 1) {
                whereClause.append(" AND ");
            }
        }
        sql.append(" WHERE ").append(whereClause);
        return sql.toString();
    }

    /**
     * 生成彻底删除语句
     */
    public static String generateDeleteSQL(Object entity) {
        // 获取实体类
        Class<?> entityClass = entity.getClass();
        // 获取表名
        String tableName = getTableName(entityClass);
        // 构建DELETE语句
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(tableName);
        // 寻找主键字段，假设有多个主键字段
        // 获取所有字段
        Field[] fields = entityClass.getDeclaredFields();
        //具有@Id注解的字段
        List<Field> idFields = new ArrayList<>();
        //字段名
        List<String> idFieldNames = new ArrayList<>();
        // 遍历所有字段
        for (Field field : fields) {
            // 判断是否有@Id注解
            if (field.isAnnotationPresent(Id.class)) {
                // 设置字段可访问
                field.setAccessible(true);
                // 获取字段名
                String fieldName = field.getAnnotation(Column.class).name();
                idFields.add(field);
                idFieldNames.add(fieldName);
            }
        }
        // 如果没有找到主键字段，则抛出异常
        if (idFields.isEmpty()) {
            throw new IllegalArgumentException("未找到主键字段");
        }
        // 构建WHERE语句
        StringBuilder whereClause = new StringBuilder();
        for (int i = 0; i < idFields.size(); i++) {
            try {
                // 获取主键字段值
                Object fieldValue = idFields.get(i).get(entity);
                //如果字段值为字符串，需要加单引号
                if (fieldValue instanceof String) {
                    whereClause.append(idFieldNames.get(i)).append(" = '").append(fieldValue).append("'");
                } else {
                    whereClause.append(idFieldNames.get(i)).append(" = ").append(fieldValue);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("无法访问字段: " + idFieldNames.get(i), e);
            }
            if (i < idFields.size() - 1) {
                whereClause.append(" AND ");
            }
        }
        sql.append(" WHERE ").append(whereClause);
        return sql.toString();
    }

    /**
     * 生成多表查询语句
     * @param entity 多表实体对象
     * @param relation 与原where条件的关系运算符
     * @param where 条件
     * @return 查询语句
     */
    public static String getMultiTableSQL(IMultiTableEntity entity, String relation, String where) {
        // 获取实体类
        Class<?> entityClass = entity.getClass();
        // 获取原始SQL语句
        String originalSQL = entity.getSQL();
        // 合成SQL语句
        if(where == null || where.isEmpty()){
            return originalSQL;
        }
        //如果原本的SQL语句中已经有where条件，则直接添加到原SQL语句中
        if(originalSQL.toLowerCase().contains("where")){
            return originalSQL + " " + relation + " " + where;
        }else {
            //如果原本的SQL语句中没有where条件，则直接添加where条件到SQL语句中
            return originalSQL + " where " + where;
        }
    }

    /**
     * 生成多表查询语句
     * @param entity 多表实体对象
     * @param where 条件
     * @return 查询语句
     */
    public static String getMultiTableSQL(IMultiTableEntity entity, String where) {
        return getMultiTableSQL(entity, "AND", where);
    }

}

