package com.fengjx.hello.distributed.lock;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author fengjianxin
 */
@Component
public class RedisLock {

    @Resource
    private StringRedisTemplate redis;

    private static final String SCRIPT = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
            "    return redis.call(\"del\",KEYS[1])\n" +
            "else\n" +
            "    return 0\n" +
            "end";

    private static final DefaultRedisScript<Integer> REDIS_SCRIPT = new DefaultRedisScript<>();

    static {
        REDIS_SCRIPT.setScriptText(SCRIPT);
        REDIS_SCRIPT.setResultType(Integer.class);
    }

    /**
     * 获取分布式锁
     *
     * @param lock    锁标识
     * @param timeout 超时时间
     * @return 获得锁的版本，需要通过锁版本释放锁
     */
    public String tryLock(String lock, long timeout) {
        UUID uuid = UUID.randomUUID();
        long threadId = Thread.currentThread().getId();
        String version = String.format("%s:%s", uuid.toString(), threadId);
        Boolean res = redis.opsForValue().setIfAbsent(lock, version, timeout, TimeUnit.MILLISECONDS);
        return res == null || !res ? null : version;
    }

    /**
     * 释放锁
     *
     * @param lock    锁标识
     * @param version 锁版本
     */
    public void releaseLock(String lock, String version) {
        redis.execute(REDIS_SCRIPT, Collections.singletonList(lock), version);
    }
}
