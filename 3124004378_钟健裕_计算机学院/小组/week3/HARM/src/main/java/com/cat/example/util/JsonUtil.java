package com.cat.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


/**
 * @author 钟健裕
 */
public class JsonUtil {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        //初始化ObjectMapper，设置序列化日期格式为时间戳为false
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //注册JavaTimeModule，支持Java8时间类型
        OBJECT_MAPPER.registerModule(new JavaTimeModule());

    }
}