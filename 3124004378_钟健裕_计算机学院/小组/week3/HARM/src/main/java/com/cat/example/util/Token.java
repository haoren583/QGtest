package com.cat.example.util;

import com.cat.example.dao.Dao;
import com.cat.example.entity.Doctor;
import com.cat.example.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import static com.cat.example.util.Base64UrlUtil.base64UrlDecode;
import static com.cat.example.util.Base64UrlUtil.base64UrlEncode;

public class Token {

    public static final Logger logger = Logger.getLogger(Token.class.getName());

    public enum Code {
        VALID,
        INVALID,
        REFRESH
    }

    // 密钥
    private static final String SECRET;
    // HmacSHA256算法
    private static Mac sha256HMAC;
    //过期时间
    private static final long EXPIRATION_TIME;
    //刷新时间//离过期时间多久刷新
    private static final long REFRESH_TIME;
    // JWT Header
    private static final String HEADER_JSON = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    // JWT Header的Base64Url编码
    private static final String ENCODED_HEADER = base64UrlEncode(HEADER_JSON.getBytes(StandardCharsets.UTF_8));

    static {
        //读取properties文件获取密钥
        Properties properties = new Properties();
        try {
            properties.load(JwtGenerator.class.getResourceAsStream("/MyToken.properties"));
            SECRET = properties.getProperty("SECRET_KEY");
            EXPIRATION_TIME = Long.parseLong(properties.getProperty("EXPIRATION_TIME"));
            REFRESH_TIME = Long.parseLong(properties.getProperty("REFRESH_TIME"));
            // 使用HmacSHA256算法生成签名
            sha256HMAC = Mac.getInstance("HmacSHA256");
            // 设置密钥，编码为UTF-8
            SecretKeySpec secretKey = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256HMAC.init(secretKey);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成签名
     * @param data 待签名的数据（Header.Payload）
     * @return 签名字符串（Base64Url编码）
     */
    private static String sign(String data) {
        // 计算签名，返回字节数组//二进制数据
        byte[] signatureBytes = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return base64UrlEncode(signatureBytes);
    }



    private String access_token;
    private Code tokenCode;
    // base64Url编码后的parts
    private String[] parts;
    // 载荷
    private Map<String, Object> payload;

    public Code getTokenCode() {
        return tokenCode;
    }

    public  Map<String, Object> getPayload() {
        return payload;
    }

    public Token() {
    }

    private Token(Code tokenCode, String access_token) {
        this.tokenCode = tokenCode;
        this.access_token = access_token;
    }

    /**
     * 生成token
     * @param payload 载荷
     * @return Token对象
     */
    public Token(Map<String, Object> payload) throws JsonProcessingException {
        //解析载荷
        String payloadJson = null;
        //插入过期时间
        payload.put("exp", System.currentTimeMillis() + EXPIRATION_TIME);
        payloadJson = JsonUtil.OBJECT_MAPPER.writeValueAsString(payload);
        this.payload = payload;
        //生成Header.Payload
        String headerAndPayload = ENCODED_HEADER + "." + base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));
        //生成签名
        String sign = sign(headerAndPayload);
        //生成完整token
        access_token = headerAndPayload + "." + sign;
        this.tokenCode = Code.REFRESH;
    }

    /**
     * 生成token
     * @param user 用户对象
     */
    public Token(User user) throws JsonProcessingException {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }
        //生成载荷
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getUserId());
        payload.put("userName", user.getUserName());
        payload.put("status", user.getStatus());
        //插入过期时间
        payload.put("exp", System.currentTimeMillis() + EXPIRATION_TIME);
        this.payload = payload;
        String payloadJson = JsonUtil.OBJECT_MAPPER.writeValueAsString(payload);
        //生成Header.Payload
        String headerAndPayload = ENCODED_HEADER + "." + base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));
        //生成签名
        String sign = sign(headerAndPayload);
        //生成完整token
        access_token = headerAndPayload + "." + sign;
        this.tokenCode = Code.REFRESH;
    }


    /**
     * 从Map中提取Token对象
     */
    public static Token getToken(Map<String, Object> map) throws JsonProcessingException {
        Token tokenPackage = new Token(Code.INVALID, null);
        tokenPackage.access_token = (String) map.get("access_token");
        if (tokenPackage.access_token == null) {
            logger.warning("Map中没有access_token");
            return tokenPackage;
        }
        //解析token
        boolean format = tokenPackage.createParts();
        if (!format) {
            logger.warning("token格式错误");
            return tokenPackage;
        }
        //解析载荷
        tokenPackage.parsePayload();
        //验证token是否有效
        boolean tokenValid = tokenPackage.verify();
        if (!tokenValid) {
            logger.warning("token已过期或签名错误");
            return tokenPackage;
        }
        //判断是否要刷新token
        long expTime = (long) tokenPackage.payload.get("exp");
        if (System.currentTimeMillis() - REFRESH_TIME > expTime){
            //刷新token
            tokenPackage.tokenCode = Code.REFRESH;
            //生成新的token
            tokenPackage.payload.put("exp", System.currentTimeMillis() + EXPIRATION_TIME);
            tokenPackage.parts[1] = base64UrlEncode(JsonUtil.OBJECT_MAPPER.writeValueAsString(tokenPackage.payload).getBytes(StandardCharsets.UTF_8));
            //生成签名
            String sign = sign(tokenPackage.parts[0] + "." + tokenPackage.parts[1]);
            //生成完整token
            tokenPackage.access_token = tokenPackage.parts[0] + "." + tokenPackage.parts[1] + "." + sign;
            logger.info(tokenPackage + "将过期,已刷新");
        }
        return tokenPackage;
    }

    /**
     * 从access_token中提取Token对象
     */
    public static Token getToken(String access_token) throws JsonProcessingException {
        if (access_token == null) {
            logger.warning("access_token为空");
            return null;
        }

        Token tokenPackage = new Token(Code.INVALID, null);
        tokenPackage.access_token = access_token;

        //解析token
        boolean format = tokenPackage.createParts();
        if (!format) {
            logger.warning("token格式错误");
            return tokenPackage;
        }
        //解析载荷
        tokenPackage.parsePayload();
        //验证token是否有效
        boolean tokenValid = tokenPackage.verify();
        if (!tokenValid) {
            logger.warning("token已过期或签名错误");
            return tokenPackage;
        }
        //判断是否要刷新token
        long expTime = (long) tokenPackage.payload.get("exp");
        if (System.currentTimeMillis() - REFRESH_TIME > expTime){
            //刷新token
            tokenPackage.tokenCode = Code.REFRESH;
            //生成新的token
            tokenPackage.payload.put("exp", System.currentTimeMillis() + EXPIRATION_TIME);
            tokenPackage.parts[1] = base64UrlEncode(JsonUtil.OBJECT_MAPPER.writeValueAsString(tokenPackage.payload).getBytes(StandardCharsets.UTF_8));
            //生成签名
            String sign = sign(tokenPackage.parts[0] + "." + tokenPackage.parts[1]);
            //生成完整token
            tokenPackage.access_token = tokenPackage.parts[0] + "." + tokenPackage.parts[1] + "." + sign;
            logger.info(tokenPackage + "将过期,已刷新");
        }
        return tokenPackage;
    }


    /**
     * 将Token对象塞入Map
     * @param map 存放Token的Map
     */
    public void putToken(Map<String, Object> map) {
        if (tokenCode == Code.REFRESH) {
            map.put("access_token", access_token);
        }
        map.put("tokenCode", tokenCode.name());
    }

    /**
     * 生成base64Url编码后的parts
     * @return 是否成功
     */
    private boolean createParts() {
        this.parts = access_token.split("\\.");
        if (parts.length != 3) {
            tokenCode = Code.INVALID;
            return false;
        }
        return true;
    }

    /**
     * 解析token获得载荷
     */
    private void parsePayload() throws JsonProcessingException {
        String payloadJson = new String(base64UrlDecode(parts[1]), StandardCharsets.UTF_8);
        payload = JsonUtil.OBJECT_MAPPER.readValue(payloadJson, Map.class);
    }

    /**
     * 验证token是否有效
     */
    public boolean verify(){
        // 验证签名
        String signTure = sign(parts[0] + "." + parts[1]);
        if (!signTure.equals(parts[2])) {
            tokenCode = Code.INVALID;
            return false;
        }else{
            // 验证过期时间
            long expTime = (long) payload.get("exp");
            if (System.currentTimeMillis() > expTime) {
                tokenCode = Code.INVALID;
                return false;
            }else{
                tokenCode = Code.VALID;
                return true;
            }
        }
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + access_token + '\'' +
                ", tokenCode=" + tokenCode +
                ", payload=" + payload +
                '}';
    }

    public static void main(String[] args) throws JsonProcessingException {
        //测试生成token
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", 1);
        payload.put("userName", "钟健裕");
        Token token = new Token(payload);
        System.out.println("token:"+token.toString());
        Map<String, Object> map = new HashMap<>();
        token.putToken(map);
        System.out.println("map:"+map.toString());
        Token token1 = Token.getToken(map);
        System.out.println("token1"+token1.toString());
    }

}
