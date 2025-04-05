package com.cat.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.cat.example.util.Base64UrlUtil.base64UrlEncode;

/**
 * @author 钟健裕
 * 用于生成和验证JWT Token的工具类
 */
public class JwtGenerator {

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

    public enum tokenCode{
        VALID,
        INVALID,
        REFRESH
    }

    public static class Token {
        private tokenCode code;
        private String token;
        private Map<String,Object> payload;

        public Token(tokenCode code, String token) throws IOException {
            this.code = code;
            this.token = token;
            this.payload = extractPayload();
        }

        public Token(Map<String,Object> map) throws IOException {
            this.payload = map;
            this.token = createJwt(map);
            this.code = tokenCode.VALID;
        }

        public Token() {

        }

        //获取payload部分
        private Map<String,Object> extractPayload() throws IOException {
            String payloadJson = new String(base64UrlDecode(token.split("\\.")[1]), StandardCharsets.UTF_8);
            return JsonUtil.OBJECT_MAPPER.readValue(payloadJson, Map.class);
        }

        //将token加入到Map中
        public void addToMap(Map<String,Object> map){
            map.put("code",code.toString());
            map.put("token",token);
        }

        //从Map中获取token
        public static Token fromMap(Map<String,Object> map) throws IOException {
            Token theToken = new Token();
            theToken.token = (String)map.get("token");
            //如果map没有code或token，抛出异常
            if(map.get("code") == null || map.get("token") == null){
                throw new IllegalArgumentException("map中缺少code或token");
            }
            //验证签名
            verifyJwt(theToken);
            //获取payload部分
            theToken.payload = theToken.extractPayload();
            return theToken;
        }

        /**
         * 获取
         * @return code
         */
        private tokenCode getCode() {
            return code;
        }

        /**
         * 设置
         * @param code
         */
        private void setCode(tokenCode code) {
            this.code = code;
        }

        /**
         * 获取
         * @return token
         */
        private String getToken() {
            return token;
        }

        /**
         * 设置
         * @param token
         */
        private void setToken(String token) {
            this.token = token;
        }

        public String toString() {
            return "Token{code = " + code + ", token = " + token + "}";
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

    /**
     * 创建JWT Token
     *
     * @param payload Payload部分的Map对象
     * @return JWT Token字符串
     */
    private static String createJwt(Map<String, Object> payload) throws JsonProcessingException {
        //生成过期时间
        long expirationTime = System.currentTimeMillis() + EXPIRATION_TIME;
        //插入过期时间
        payload.put("exp", expirationTime);
        //生成json格式的payload，并使用Base64Url编码
        String encodedPayload = base64UrlEncode(JsonUtil.OBJECT_MAPPER.writeValueAsBytes(payload));
        // 生成签名
        String signature = sign(ENCODED_HEADER + "." + encodedPayload);
        return ENCODED_HEADER + "." + encodedPayload + "." + signature;
    }



    /**
     * Base64Url编码
     * @param input 输入的字节数组
     * @return Base64Url编码后的字符串
     */
    private static String base64UrlEncode(byte[] input) {
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
    private static byte[] base64UrlDecode(String input) {
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
     * 验证JWT Token的签名
     * @param tokenPackage Token的Header、Payload、签名,和tokenCode
     * @return 如果token有效，返回true；否则返回false
     */
    public static boolean verifyJwt(Token tokenPackage) throws JsonProcessingException {
        String newToken = tokenPackage.token;
        boolean isOK = false;
        String[] parts = newToken.split("\\.");
        if (parts.length != 3) {
            tokenPackage.code = tokenCode.INVALID;
            return false;
        }
        //获取payload部分
        String payloadJson = new String(base64UrlDecode(parts[1]), StandardCharsets.UTF_8);
        // 将Payload JSON字符串转换为Map对象
        Map<String, Object> payload = JsonUtil.OBJECT_MAPPER.readValue(payloadJson, Map.class);
        //验证签名
        String signature = sign(parts[0] + "." + parts[1]);
        if (!signature.equals(parts[2])) {
            tokenPackage.code = tokenCode.INVALID;
            return false;
        }
        //验证过期时间
        long expirationTime = (long) payload.get("exp");
        if (System.currentTimeMillis() > expirationTime) {
            tokenPackage.code = tokenCode.INVALID;
            return false;
        }
        //判断是否需要刷新Token
        if(expirationTime - System.currentTimeMillis() < REFRESH_TIME){
            //需要刷新Token
            tokenPackage.code = tokenCode.REFRESH;
            tokenPackage.token = refreshToken(newToken);
            return true;
        }else{
            //不需要刷新Token
            tokenPackage.code = tokenCode.VALID;
            return true;
        }
    }

    //刷新Token
    private static String refreshToken(String token) {
        // 解码Token
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return null;
        }
        // 获取Payload部分
        String payloadJson = new String(base64UrlDecode(parts[1]), StandardCharsets.UTF_8);
        try {
            // 将Payload JSON字符串转换为Map对象
            Map<String, Object> payload = JsonUtil.OBJECT_MAPPER.readValue(payloadJson, Map.class);
            // 创建新的JWT
            return createJwt(payload);
        } catch (IOException e) {
            throw new RuntimeException("无法解析或编码Payload", e);
        }
    }

    public static Token createToken(Map<String, Object> payload) throws JsonProcessingException {
        Token token = new Token();
        token.token = createJwt(payload);
        token.code = tokenCode.VALID;
        return token;
    }



    // 示例主方法
    public static void main(String[] args) throws IOException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", 123);
        payload.put("userName", "钟健裕");
        payload.put("role", "admin");
        System.out.println("payload: "+payload);
        byte[] bytes1 = JsonUtil.OBJECT_MAPPER.writeValueAsBytes(payload);
        System.out.println("bytes1: "+ new String(bytes1));
        String json = JsonUtil.OBJECT_MAPPER.writeValueAsString(payload);
        System.out.println(json);
        byte[] bytes2 = json.getBytes(StandardCharsets.UTF_8);
        System.out.println("bytes2: "+ new String(bytes2));
        String encodedPayload = base64UrlEncode(bytes1);
        System.out.println("encodedPayload: "+encodedPayload);
        String signature = sign(ENCODED_HEADER + "." + encodedPayload);
        System.out.println("signature: "+signature);
        String token = ENCODED_HEADER + "." + encodedPayload + "." + signature;
        System.out.println("token: "+token);

        // 创建JWT Token
        String token1 = createJwt(payload);
        System.out.println("token1: "+token1);

        // 验证JWT Token的签名
        Token tokenPackage = new Token();
        tokenPackage.token = token1;
        boolean isOK = verifyJwt(tokenPackage);
        System.out.println("isOK: "+isOK);
        System.out.println("tokenPackage: "+tokenPackage);

        Token token2 = createToken(payload);
        System.out.println("token2: "+token2);

        //解析Token
        Map<String,Object> map = new HashMap<>();
        token2.addToMap(map);
        System.out.println("map: "+map);
        try {
            token2.fromMap(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("token2: "+token2);
        System.out.println("token2.payload: "+token2.extractPayload());
    }
}