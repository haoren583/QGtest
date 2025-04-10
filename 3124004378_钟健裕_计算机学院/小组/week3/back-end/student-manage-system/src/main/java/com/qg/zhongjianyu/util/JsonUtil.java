package com.qg.zhongjianyu.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author 钟健裕
 */
public class JsonUtil {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final SimpleModule  module = new SimpleModule();
    static {
        //初始化ObjectMapper，设置序列化日期格式为时间戳为false
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //注册JavaTimeModule，支持Java8时间类型
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        //允许反序列化时的未知属性
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 创建一个SimpleModule，并将long类型序列化为字符串
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Double.class, ToStringSerializer.instance);
        module.addSerializer(Float.class, ToStringSerializer.instance);
        // 注册模块
        OBJECT_MAPPER.registerModule(module);
    }

    public static void main(String[] args) throws JsonProcessingException {
        Time time = Time.valueOf("12:00:00");
        Map<String,Object> map11=new HashMap<>();
        map11.put("value1",time);
        String json1 = OBJECT_MAPPER.writeValueAsString(map11);
        System.out.println(json1);

        Map<String, Object> map22 = new HashMap<>();
        map22.put("value2", time);
        String json2 = OBJECT_MAPPER.writeValueAsString(map22);
        System.out.println(json2);

        Map<String, Object> map1 = OBJECT_MAPPER.readValue(json1, Map.class);
        System.out.println(map1);
        Map<String, Object> map2 = OBJECT_MAPPER.readValue(json2, Map.class);
        System.out.println(map2);

        Time time1 = Time.valueOf((String) map1.get("value1"));
        System.out.println(time1);
        Time time2 = Time.valueOf((String) map2.get("value2"));
        System.out.println(time2);
        LocalDate data1 = LocalDate.parse("2021-01-01");
        System.out.println(data1);
        Object a = null;
        String b = (String) a;
        //LocalDate date = LocalDate.parse((String)a);
        System.out.println(b);
    }
}