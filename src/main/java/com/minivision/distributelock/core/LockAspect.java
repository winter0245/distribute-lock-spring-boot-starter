package com.minivision.distributelock.core;

import com.minivision.distributelock.annotation.DistributeLock;
import com.minivision.distributelock.annotation.LockParam;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

@Slf4j
@Aspect
//提高优先级
@Order(-1)
public class LockAspect {
    @Autowired
    private RedissonClient redissonClient;

    private Map<String, Lock> lockCacheMap = new ConcurrentHashMap<>();

    private Lock computeLock(DistributeLock distributeLock, String key) {
        if (distributeLock.lockType() == LockType.READ_LOCK || distributeLock.lockType() == LockType.WRITE_LOCK) {
            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(key);
            if (distributeLock.lockType() == LockType.WRITE_LOCK) {
                return readWriteLock.writeLock();
            }
            return readWriteLock.readLock();
        } else if (distributeLock.lockType() == LockType.FAIR_LOCK) {
            return redissonClient.getFairLock(key);
        } else {
            return redissonClient.getLock(key);
        }
    }

    private Lock getLock(DistributeLock distributeLock) {
        String key = distributeLock.lockName() + distributeLock.lockType();
        return lockCacheMap.computeIfAbsent(key, k -> computeLock(distributeLock, distributeLock.lockName()));
    }

    private Lock getParamLock(DistributeLock distributeLock, ProceedingJoinPoint joinPoint) {
        StringBuilder keyBuilder = new StringBuilder(distributeLock.lockName());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();
        //遍历参数，将lockParam注解的参数加入到key中
        boolean hasParam = false;
        for (int i=0;i<parameters.length;i++) {
            Parameter parameter=parameters[i];
            LockParam lockParam = parameter.getAnnotation(LockParam.class);
            if (lockParam != null) {
                Object arg=args[i];
                hasParam = true;
                keyBuilder.append("-");
                if(arg==null) {
                    keyBuilder.append("NULL");
                    continue;
                }
                    if (lockParam.value() == ParamType.TOSTRING) {
                        keyBuilder.append(arg.toString());
                    } else {
                        keyBuilder.append(arg.hashCode());
                    }
            }
        }
        if (!hasParam) {
            log.warn("method {} can't find parameter with LockParam annotation ", signature);
        }
        return computeLock(distributeLock, keyBuilder.toString());
    }

    @Around("@annotation(distributeLock)")
    public Object invokeLockMethod(ProceedingJoinPoint joinPoint, DistributeLock distributeLock) throws Throwable {
        Lock lock = null;
        if (distributeLock.hasParamLock()) {
            lock = getParamLock(distributeLock, joinPoint);
        } else {
            lock = getLock(distributeLock);
        }
        try {
            log.debug("start invoke method {} , DistributeLock :{} , try to get lock", joinPoint.getSignature(), distributeLock);
            lock.lock();
            log.debug("invoke method {} get lock ", joinPoint.getSignature());
            return joinPoint.proceed();
        } catch (Throwable e) {
            log.error("invoke error", e);
            throw e;
        } finally {
            lock.unlock();
            log.debug("invoke method {} end", joinPoint.getSignature());
        }
    }

}
