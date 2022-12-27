package com.fengjx.hello.distributed.tx.pay.service;

import com.fengjx.hello.distributed.tx.pay.mq.TxRocketMQTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author FengJianxin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayService {


    private final TxRocketMQTemplate txRocketMQTemplate;


    public void vipPayTxByMq(long userId) {
        long now = System.currentTimeMillis();
        String tid = UUID.randomUUID().toString();
        String body = tid + "_" + userId;
        Message<String> message = MessageBuilder.withPayload(body).build();
        txRocketMQTemplate.sendMessageInTransaction("vip_pay", message, String.valueOf(now));
        log.info("send vip pay msg: {}", userId);
    }

}
