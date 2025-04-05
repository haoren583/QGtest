package com.cat.example.util;

import java.util.Base64;

/**
 * Base64Url编码工具类
 * @author 钟健裕
 */
public class Base64UrlUtil {

    /**
     * Base64Url编码
     * @param input 输入的字节数组
     * @return Base64Url编码后的字符串
     */
    public static String base64UrlEncode(byte[] input) {
        // 标准Base64编码
        //将传入的字节数组 input 编码为Base64字符串格式
        //然后，将Base64字符串中的 + / 字符替换为 - _ ，并去掉末尾的 = 字符
        String base64 = Base64.getEncoder().encodeToString(input);
        // 替换URL不安全的字符
        return base64.replace('+', '-')
                .replace('/', '_')
                .replaceAll("=+$", "");
    }

    /**
     * Base64Url解码
     * @param input Base64Url编码的字符串
     * @return 解码后的字节数组
     */
    public static byte[] base64UrlDecode(String input) {
        // 标准Base64解码
        // 将Base64Url编码的字符串 input 解码为字节数组
        // 先将 + / 字符替换为 - _
        String base64 = input.replace('-', '+')
                .replace('_', '/')
                // 添加填充字符
                // 因为Base64Url编码后可能出现长度不为4的倍数的字符串，
                // 所以需要在末尾添加填充字符，使其长度为4的倍数
                // 这里使用"="作为填充字符
                .concat("=".repeat((4 - input.length() % 4) % 4));
        return Base64.getDecoder().decode(base64);
    }

    /**
     * Base64Url编码
     * @param input 输入的字符串
     * @return Base64Url编码后的字符串
     * 字符串转换为字节数组，然后调用 {@link #base64UrlEncode(byte[])} 方法进行编码
     */
    public static String base64UrlEncode(String input) {
        return base64UrlEncode(input.getBytes());
    }
}
