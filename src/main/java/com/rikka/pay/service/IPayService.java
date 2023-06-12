package com.rikka.pay.service;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import com.rikka.pay.pojo.PayInfo;

import java.math.BigDecimal;

/**
 * @author OhtoAi
 * 2023/5/26 9:51
 */
public interface IPayService {

    /**
     *
     * @param orderId 订单号
     * @param money 付款金额
     * @param bestPayTypeEnum  支付类型
     * @return 支付平台响应信息
     */
    PayResponse createPayment(String orderId, BigDecimal money, BestPayTypeEnum bestPayTypeEnum);

    /**
     *
     * @param notifyData WeChat Pay result
     * @return Response to WeChat Pay
     */
    String asyncNotify(String notifyData);

    /**
     *
     * @param orderNo 关联的订单号
     * @return 查询到的结果
     */
    PayInfo queryByOrderId(Long orderNo);
}
