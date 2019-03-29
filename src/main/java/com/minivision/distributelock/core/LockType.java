package com.minivision.distributelock.core;

/**
 * 锁类型枚举类
 *
 * @Auther: zhangdongdong
 * @Date: 2019/3/15 0015 16:44
 * @Description:
 */
public enum LockType {
    /**
     * redis写锁
     */
    WRITE_LOCK,
    /**
     * redis读锁
     */
    READ_LOCK,
    /**
     * redis公平锁
     */
    FAIR_LOCK,
    /**
     * redis非公平锁
     */
    UNFAIR_LOCK



}
