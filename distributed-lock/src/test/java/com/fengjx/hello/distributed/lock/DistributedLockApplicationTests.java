package com.fengjx.hello.distributed.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class DistributedLockApplicationTests {

    @Resource
    private ThreadPoolTaskExecutor executor;
    @Resource
    private ZkLock zkLock;

    @Test
    void contextLoads() {
    }

    @Test
    public void testZkLock() throws Exception {
        try {
            for (int i = 0; i < 50; i++) {
                int finalI = i;
                executor.submit(() -> {
                    InterProcessMutex mutex = zkLock.createInterProcessMutex("test_lock");
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


}
