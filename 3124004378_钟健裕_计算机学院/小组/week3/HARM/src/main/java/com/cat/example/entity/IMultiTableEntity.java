package com.cat.example.entity;

import com.cat.example.dao.ORM.Entity;

import java.sql.ResultSet;

/**
 * 多表查询的实体接口
 * @author 钟健裕
 */
public interface IMultiTableEntity {
    /**
     * 获取SQL语句
     * @return SQL语句
     */
    public String getSQL();

    /**
     * 添加查询条件并获取SQL语句
     * @param relation 与原where条件的关系运算符
     * @param where 条件
     * @return SQL语句
     */
     default public String getSQL(String relation , String where){
         //如果where条件为空，则直接返回原SQL语句
         if(where == null || where.isEmpty()){
             return getSQL();
         }
         //如果原本的SQL语句中已经有where条件，则直接添加到原SQL语句中
         if(getSQL().toLowerCase().contains("where")){
             return getSQL() + " " + relation + " " + where;
         }else{
             //如果原本的SQL语句中没有where条件，则直接添加where条件到SQL语句中
             return getSQL() + " where " + where;
         }
     }

    /**
     * 添加查询条件并获取SQL语句,与原where条件的关系运算符默认为and
     * @param where 条件
     * @return SQL语句
     */
    default public String getSQL(String where){
        return getSQL("and", where);
    }
}
