package com.fengjx.hello.distributed.lock;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author fengjianxin
 */
@Component
public class RedisLock implements Lock<String> {

    @Resource
    private StringRedisTemplate redis;


    @Override
    public boolean tryLock(String lock, long timeout) {
        return false;
    }

    @Override
    public void releaseLock(String lock) {

    }
}
