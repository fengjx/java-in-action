package com.fengjx.hello.distributed.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class DistributedLockApplicationTests {


    private static final String LOCK_KEY = "test_lock";

    @Resource
    private ThreadPoolTaskExecutor executor;
    @Resource
    private ZkLock zkLock;
    @Resource
    private DbLock dbLock;
    @Resource
    private RedisLock redisLock;

    @Test
    void contextLoads() {
    }

    @Test
    public void testZkLock() throws Exception {
        try {
            for (int i = 0; i < 50; i++) {
                int finalI = i;
                executor.submit(() -> {
                    InterProcessMutex mutex = zkLock.createInterProcessMutex(LOCK_KEY);
                    if (zkLock.tryLock(mutex, 10)) {
                        try {
                            log.info("do task: {}", finalI);
                            TimeUnit.MILLISECONDS.sleep(10);
                        } finally {
                            zkLock.releaseLock(mutex);
                        }
                    } else {
                        log.info("can't get lock: {}", finalI);
                    }
                    return finalI;
                });
            }
        } finally {
            TimeUnit.SECONDS.sleep(10);
            executor.shutdown();
            log.info("=================finally==================");
        }
    }

    @Test
    public void testDbLock() throws Exception {
        try {
            for (int i = 0; i < 50; i++) {
                int finalI = i;
                executor.submit(() -> {
                    int version = dbLock.tryLock(LOCK_KEY, 1000);
                    if (version > DbLock.FAIL_VERSION) {
                        try {
                            log.info("do task: {}", finalI);
                            TimeUnit.MILLISECONDS.sleep(10);
                        } finally {
                            dbLock.releaseLock(LOCK_KEY, version);
                        }
                    } else {
                        log.info("can't get lock: {}", finalI);
                    }
                    return finalI;
                });
            }
        } finally {
            TimeUnit.SECONDS.sleep(10);
            executor.shutdown();
            log.info("=================finally==================");
        }
    }

    @Test
    public void testDbLock2() throws Exception {
        int version = dbLock.tryLock(LOCK_KEY, 10000);
        int version2 = dbLock.tryLock(LOCK_KEY, 10000);
        dbLock.releaseLock(LOCK_KEY, version);
    }


    @Test
    public void testRedisLock() {
        String lock = redisLock.tryLock("test_redis_lock", 60000);
        log.info("lock: {}", lock);
        redisLock.releaseLock("test_redis_lock", lock);
    }

    @Test
    public void testRedisLock2() throws Exception {
        try {
            for (int i = 0; i < 1000; i++) {
                int finalI = i;
                executor.submit(() -> {
                    String version = redisLock.tryLock(LOCK_KEY, 1000);
                    if (!StringUtils.isEmpty(version)) {
                        try {
                            log.info("do task: {}", finalI);
                            TimeUnit.MILLISECONDS.sleep(1);
                        } finally {
                            redisLock.releaseLock(LOCK_KEY, version);
                        }
                    } else {
                        log.info("can't get lock: {}", finalI);
                    }
                    return finalI;
                });
            }
        } finally {
            TimeUnit.SECONDS.sleep(10);
            executor.shutdown();
            log.info("=================finally==================");
        }
    }

}
