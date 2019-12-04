package com.fengjx.hello.distributed.lock;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author fengjianxin
 */
@Component
public class RedisLock {

    @Resource
    private StringRedisTemplate redis;


    public boolean tryLock(String lock, long timeout) {
        return false;
    }

    public void releaseLock(String lock) {

    }
}
