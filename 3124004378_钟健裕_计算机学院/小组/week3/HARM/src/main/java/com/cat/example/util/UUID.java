package com.cat.example.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 钟健裕
 * UUID工具类
 * 这个UUID是一个long类型的数字，由时间戳，机器码，序列号组成
 */
public class UUID {
    // 时间戳位数（毫秒级，支持到公元10889年）
    private static final int TIMESTAMP_BITS = 46;
    private static final long TIMESTAMP_MASK = (1L << TIMESTAMP_BITS) - 1;
    // 机器ID位数（最多1024个节点）
    private static final int MACHINE_ID_BITS = 6;
    private static final long MACHINE_ID_MASK = (1L << MACHINE_ID_BITS) - 1;
    // 序列号位数（每毫秒最多4096个ID）
    private static final int SEQUENCE_BITS = 12;
    private static final long SEQUENCE_MASK = (1L << SEQUENCE_BITS) - 1;

    // 时间戳左移位数
    private static final int TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;
    // 机器ID左移位数
    private static final int MACHINE_ID_SHIFT = SEQUENCE_BITS;
    // 当前时间戳//毫秒数
    private static long CURRENT_TIMESTAMP = System.currentTimeMillis();
    // 机器ID
    private static final int MACHINE_ID = 1;
    // 序列号//每毫秒生成的ID数量//用 AtomicInteger 实现线程安全
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    /**
     * 生成UUID
     * @return UUID
     */
    public static long generateUUID() {
        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();
        //判断是否是同一毫秒
        if (timestamp == CURRENT_TIMESTAMP) {
            //如果是同一毫秒，则获取并自增序列号
            long sequence = SEQUENCE.incrementAndGet() & (SEQUENCE_MASK);
            //生成UUID
            return (timestamp << TIMESTAMP_SHIFT) | (MACHINE_ID << MACHINE_ID_SHIFT) | sequence;
        }else{
            //如果不是同一毫秒，则重置序列号
            SEQUENCE.set(0);
            //更新当前时间戳
            CURRENT_TIMESTAMP = timestamp;
            //生成UUID
            return (timestamp << TIMESTAMP_SHIFT) | (MACHINE_ID << MACHINE_ID_SHIFT);
        }
    }
}
