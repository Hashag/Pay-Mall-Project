package com.rikka.pay.service.serviceimpl;

import com.google.gson.Gson;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.rikka.pay.dao.PayInfoMapper;
import com.rikka.pay.enums.PayPlatformEnum;
import com.rikka.pay.pojo.PayInfo;
import com.rikka.pay.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author OhtoAi
 * 2023/5/26 10:11
 */

@Service
@Slf4j
public class IPayServiceImpl implements IPayService {

    private static final String EXCHANGE_NAME = "pay-exchange";
    private static final String ROUTING_KEY = "pay-routing-key";

    /**
     * 序列化PayInfo对象
     */
    private Gson gson = new Gson();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BestPayService bestPayService;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Override
    public PayResponse createPayment(String orderId, BigDecimal money, BestPayTypeEnum bestPayTypeEnum) {
        log.info("创建支付记录: 订单号 {}", orderId);
        // 支付信息
        PayInfo payInfo = new PayInfo();
        payInfo.setOrderNo(Long.parseLong(orderId));
        payInfo.setPayAmount(money);
        payInfo.setPayPlatform(PayPlatformEnum.getPlatform(bestPayTypeEnum).getCode());
        payInfo.setPlatformStatus(OrderStatusEnum.NOTPAY.name());

        payInfoMapper.insertSelective(payInfo);

        // 支付请求
        PayRequest payRequest = new PayRequest();
        payRequest.setPayTypeEnum(bestPayTypeEnum);
        payRequest.setOrderId(orderId);
        payRequest.setOrderName("微信公众账号支付订单");
        payRequest.setOrderAmount(money.doubleValue());
        PayResponse pay = bestPayService.pay(payRequest);
        log.info("收到的支付响应: {}", pay);
        return pay;
    }


    @Override
    public String asyncNotify(String notifyData) {
        // 1. verify sign
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("支付签名校验成功， 校验信息: {}", payResponse);

        // 2. verify money & 3. modify pay status
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(payResponse.getOrderId()));
        if (payInfo == null) {
            // alert
            throw new RuntimeException("异步通知中无法从数据库找到订单号" + "， 支付流水号： " + payResponse.getOutTradeNo());
        }
        if (!OrderStatusEnum.SUCCESS.name().equals(payInfo.getPlatformStatus())) {
            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0) {
                // alert
                throw new RuntimeException("异步通知中金额与数据库不一致" + "， 订单号 " + payInfo.getOrderNo());
            }
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());
            payInfoMapper.updateByPrimaryKeySelective(payInfo);
        }

        // 4. 发送消息到RabbitMQ, mall 模块
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, gson.toJson(payInfo));

        // 5. return response status
        if (BestPayPlatformEnum.WX == payResponse.getPayPlatformEnum()) {
            //WeChatPay V2 Interface
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        } else if (BestPayPlatformEnum.ALIPAY == payResponse.getPayPlatformEnum()) {
            // AliPay, but won't use.
            return "alipay";
        }
        throw new RuntimeException("异步通知中不受支持的支付平台");
    }

    @Override
    public PayInfo queryByOrderId(Long orderNo) {
        return payInfoMapper.selectByOrderNo(orderNo);
    }
}
