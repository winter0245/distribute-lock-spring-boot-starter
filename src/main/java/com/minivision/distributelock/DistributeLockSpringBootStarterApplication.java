package com.minivision.distributelock;

import com.minivision.distributelock.annotation.EnableDistributeLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableDistributeLock
public class DistributeLockSpringBootStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributeLockSpringBootStarterApplication.class, args);
    }

    public static ApplicationContext run(){
        return  SpringApplication.run(DistributeLockSpringBootStarterApplication.class, new String[0]);
    }
}
