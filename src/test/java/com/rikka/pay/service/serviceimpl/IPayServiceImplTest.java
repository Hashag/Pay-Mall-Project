package com.rikka.pay.service.serviceimpl;

import com.rikka.pay.PayApplicationTests;
import com.rikka.pay.service.IPayService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author OhtoAi
 * 2023/5/26 10:30
 */
@DisplayName("支付模块")
class IPayServiceImplTest extends PayApplicationTests {

    private static final String EXCHANGE_NAME = "pay-exchange";
    private static final String ROUTING_KEY = "pay-routing-key";
    private static final String QUEUE_NAME = "pay-notify-queue";

    @Autowired
    private IPayService payService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    void createPayment() {
//        payService.createPayment("11213131414141", BigDecimal.valueOf(0.01));
    }


    @DisplayName("测试MQ发送消息")
    void mqSend() {
        amqpTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, "hello");
    }
}