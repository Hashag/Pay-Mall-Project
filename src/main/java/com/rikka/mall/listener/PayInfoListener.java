package com.rikka.mall.listener;

import com.google.gson.Gson;
import com.rikka.mall.external.PayInfo;
import com.rikka.mall.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听RabbitMQ指定队列的消息
 *
 * @author Yuno
 * @time 10:55 PM 6/11/2023
 */

@RabbitListener(queues = {"pay-notify-queue"})
@Component
@Slf4j
public class PayInfoListener {

    @Autowired
    private IOrderService ios;

    /**
     * 标记上 @RabbitHandler注解, 用于处理监听到的消息
     */
    @RabbitHandler
    public void listen(String msg) {
        log.info("the msg is: {}", msg);
        Gson gson = new Gson();
        PayInfo payInfo = gson.fromJson(msg, PayInfo.class);
        if ("SUCCESS".equals(payInfo.getPlatformStatus())) {
            ios.paid(payInfo.getOrderNo());
        }
    }
}
