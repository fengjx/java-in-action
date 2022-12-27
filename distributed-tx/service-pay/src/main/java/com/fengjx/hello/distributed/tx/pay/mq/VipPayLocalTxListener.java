package com.fengjx.hello.distributed.tx.pay.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author FengJianxin
 */
@Slf4j
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "txRocketMQTemplate")
public class VipPayLocalTxListener implements RocketMQLocalTransactionListener {

    private final AtomicInteger transactionIndex = new AtomicInteger(0);

    /**
     * 执行本地事务
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(final Message msg, final Object arg) {
        int incr = transactionIndex.getAndIncrement();
        String body = new String((byte[]) msg.getPayload());
        log.info("exec local tx: {}, {}", body, incr);
        if (incr % 2 == 0) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.UNKNOWN;
    }


    /**
     * 检查本地事务是否已经完成
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(final Message msg) {
        int incr = transactionIndex.getAndIncrement();
        String body = new String((byte[]) msg.getPayload());
        log.info("check local tx: {}, {}", body, incr);
        if (incr % 2 == 0) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.UNKNOWN;
    }
}
