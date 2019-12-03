package com.fengjx.hello.distributed.lock;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author fengjianxin
 */
@Component
public class DbLock implements Lock<String> {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean tryLock(String lock, long timeout) {
        return false;
    }

    @Override
    public void releaseLock(String lock) {

    }
}
