package com.minivision.distributelock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributeLockSpringBootStarterApplicationTests {


    @Autowired
    RedisProperties redisProperties;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Test
    public void contextLoads() throws InterruptedException {

    }



}
