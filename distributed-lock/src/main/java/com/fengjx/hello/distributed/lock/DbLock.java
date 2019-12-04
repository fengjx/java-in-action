package com.fengjx.hello.distributed.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fengjianxin
 */
@Slf4j
@Component
public class DbLock {

    public static final int FAIL_VERSION = 0;

    private static final String SQL_SELECT = "select * from sys_lock where lock_key = ? order by version desc limit 1";
    private static final String SQL_INSERT = "insert into sys_lock (lock_key, version, timeout, create_time) values (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "update sys_lock set version = ?, timeout = ? where lock_key = ? and version = ?";
    private static final String SQL_DELETE = "delete from sys_lock where lock_key = ? and version = ?";


    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取锁
     *
     * @param lock    锁标识
     * @param timeout 锁超时时间
     * @return 获得锁的版本号
     */
    public int tryLock(String lock, long timeout) {
        List<SysLockEntity> list = jdbcTemplate.query(SQL_SELECT, SysLockEntity.TbLockMapper.newTbLockMapper(), lock);
        SysLockEntity sysLockEntity = CollectionUtils.isEmpty(list) ? null : list.get(0);
        long now = System.currentTimeMillis();
        timeout += now;
        if (sysLockEntity == null) {
            try {
                jdbcTemplate.update(SQL_INSERT, lock, 1, timeout, now);
                log.debug("success to get lock by insert - lock_key: {}, version: {}", lock, 1);
                return 1;
            } catch (Exception e) {
                log.warn("fail to get lock by insert - lock_key: {}, version: {}, cause: {}", lock, 1, e.getMessage());
                return FAIL_VERSION;
            }
        } else if (sysLockEntity.getTimeout() > now) {
            log.warn("fail to get lock, locking now - lock_key: {}, version: {}", lock, sysLockEntity.getVersion());
            // 锁未释放
            return FAIL_VERSION;
        }

        int version = sysLockEntity.getVersion();
        int newVersion = version + 1;
        try {
            int res = jdbcTemplate.update(SQL_UPDATE, newVersion, timeout, lock, version);
            if (res == 1) {
                log.debug("success to get lock by update - lock_key: {}, version: {}", lock, newVersion);
                return newVersion;
            } else {
                log.warn("fail to get lock by update - lock_key: {}, version: {}", lock, newVersion);
            }
        } catch (Exception e) {
            log.warn("fail to get lock by update: lock_key: {}, version: {}, cause: {}", lock, newVersion, e.getMessage());
        }
        return FAIL_VERSION;
    }

    /**
     * 释放锁
     *
     * @param lock    锁标识
     * @param version 锁版本
     */
    public void releaseLock(String lock, int version) {
        try {
            jdbcTemplate.update(SQL_DELETE, lock, version);
            log.info("success to release lock - lock_key: {}, version: {}", lock, version);
        } catch (Exception e) {
            log.error("fail to release lock - lock_key: {}, version: {}", lock, version);
        }

    }
}
