package com.minivision.distributelock.annotation;

import com.minivision.distributelock.core.LockType;

import java.lang.annotation.*;

/**
 * 对方法使用分布式锁
 * @Auther: zhangdongdong
 * @Date: 2019/3/15 0015 16:42
 * @Description:
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributeLock {
    /**
     * 设置锁类型
     * @return 锁类型
     */
    LockType lockType () default LockType.WRITE_LOCK;

    /**
     * 设置锁名称
     * @return 锁名称
     */
    String lockName();

    /**
     * \是否包含参数化锁
     * @return true-包含,false-不包含
     */
    boolean hasParamLock() default false;
}
