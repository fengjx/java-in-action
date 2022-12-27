package com.fengjx.hello.distributed.tx.pay;

import com.fengjx.hello.distributed.tx.pay.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author FengJianxin
 */
@Slf4j
@SpringBootTest
public class MqTxTest {

    @Resource
    private PayService payService;

    /**
     * 分布式事务：充值 vip 场景
     */
    @Test
    public void testVipPay() {
        payService.vipPayTxByMq(1000L);
    }


}
