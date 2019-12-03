package com.fengjx.hello.distributed.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author fengjianxin
 */
@Component
public class ZkLock implements Lock<InterProcessMutex> {

    @Resource
    private CuratorFramework client;

    private static final String BASE_PATH = "/locks/";

    /**
     * 创建互斥锁
     */
    public InterProcessMutex createInterProcessMutex(String lockPath) {
        return new InterProcessMutex(client, BASE_PATH + lockPath);
    }

    @Override
    public boolean tryLock(InterProcessMutex lock, long timeout) throws Exception {
        return lock.acquire(timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public void releaseLock(InterProcessMutex lock) throws Exception {
        lock.release();
    }
}
