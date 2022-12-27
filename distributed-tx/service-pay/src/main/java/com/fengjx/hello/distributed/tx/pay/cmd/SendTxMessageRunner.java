package com.fengjx.hello.distributed.tx.pay.cmd;

import com.fengjx.hello.distributed.tx.pay.service.PayService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author FengJianxin
 */
@Component
public class SendTxMessageRunner implements CommandLineRunner {

    @Resource
    private PayService payService;


    @Override
    public void run(final String... args) throws Exception {
        for (int i = 0; i < 3; i++) {
            payService.vipPayTxByMq(1000L + i);
        }
    }

}
