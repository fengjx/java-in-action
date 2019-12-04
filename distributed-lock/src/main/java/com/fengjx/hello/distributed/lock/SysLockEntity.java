package com.fengjx.hello.distributed.lock;

import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author fengjianxin
 */
@Getter
@Setter
public class SysLockEntity {

    private String lockKey;

    private int version;

    private long timeout;

    private long createTime;


    public static class TbLockMapper implements RowMapper<SysLockEntity> {

        public static TbLockMapper newTbLockMapper() {
            return new TbLockMapper();
        }

        @Override
        public SysLockEntity mapRow(ResultSet resultSet, int i) throws SQLException {
            SysLockEntity sysLockEntity = new SysLockEntity();
            sysLockEntity.setLockKey(resultSet.getString("lock_key"));
            sysLockEntity.setVersion(resultSet.getInt("version"));
            sysLockEntity.setTimeout(resultSet.getLong("timeout"));
            sysLockEntity.setCreateTime(resultSet.getLong("create_time"));
            return sysLockEntity;
        }
    }

}
