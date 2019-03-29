package com.minivision.distributelock.annotation;

import com.minivision.distributelock.core.ParamType;

import java.lang.annotation.*;

/**
 * 分布式锁参数注解
 * 被注解的参数会加入到锁名中，这样在高并发的情况下操作不同的参数可以提高性能
 * 注意：相同的lockName如果使用参数化的情况下，不同方法参数列表顺序一定要一致
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LockParam {
    ParamType value() default ParamType.TOSTRING;
}
