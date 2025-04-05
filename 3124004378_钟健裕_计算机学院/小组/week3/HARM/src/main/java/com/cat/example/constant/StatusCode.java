package com.cat.example.constant;

import java.util.HashMap;
import java.util.Map;

public enum StatusCode {
    Common(0),
    Doctor(1),
    Admin(2),
    isDel(3);

    private final int value;

    StatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    //将该枚举类的所有键值对转换为Map
    public static Map<String, Integer> toMap() {
        Map<String, Integer> map = new HashMap<>();
        for (StatusCode statusCode : StatusCode.values()) {
            map.put(statusCode.name(), statusCode.getValue());
        }
        return map;
    }

    public static void main(String[] args) {
        System.out.println(StatusCode.toMap());
    }
}