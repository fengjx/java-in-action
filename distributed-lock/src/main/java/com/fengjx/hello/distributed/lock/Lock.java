package com.fengjx.hello.distributed.lock;

/**
 * @author fengjianxin
 */
public interface Lock<T> {

    /**
     * 获取锁
     * @param lock 根据lockKey区分不同业务的锁
     * @param timeout 超时释放时间
     * @return true: 成功获得锁，false: 获取锁失败
     */
    boolean tryLock(T lock, long timeout) throws Exception;

    /**
     * 释放锁
     */
    void releaseLock(T lock) throws Exception;

}
