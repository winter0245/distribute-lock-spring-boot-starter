package com.minivision.distributelock.annotation;

import com.minivision.distributelock.core.LockConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启分布式锁的开关，一般加在项目启动类上
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LockConfigure.class})
public @interface EnableDistributeLock {
}
