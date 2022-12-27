package com.fengjx.hello.distributed.tx.user.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author FengJianxin
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "vip_pay", consumerGroup = "CID_user")
public class VipPayConsumer implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(final MessageExt message) {
        String msg = new String(message.getBody());
        log.info("vip pay msg: {}", msg);
        // 1. 幂等判断
        // 2. 数据库操作
    }

}
