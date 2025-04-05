package DataBaseConnectPool.ORM;

import java.lang.annotation.*;

/**
 * 这是DataEntity的注解类，用于标识一个类为数据库实体类。
 * @author 钟健裕
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataEntity {
    // 数据库表名
    String tableName();
}
