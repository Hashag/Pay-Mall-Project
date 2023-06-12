package com.rikka.pay.controller;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import com.rikka.pay.config.WXPayBean;
import com.rikka.pay.pojo.PayInfo;
import com.rikka.pay.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author OhtoAi
 * 2023/5/26 11:14
 */

@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private IPayService ips;

    @Autowired
    private WXPayBean wxPayBean;

    /**
     * @param orderId     关联的订单号
     * @param money       支付的金额
     * @param payTypeEnum 具体的支付类型： WXPAY_NATIVE
     * @return 转到的视图
     */
    @GetMapping("/create")
    public ModelAndView createPayment(@RequestParam("orderId") String orderId,
                                      @RequestParam("amount") BigDecimal money,
                                      @RequestParam("payType") BestPayTypeEnum payTypeEnum) {
        PayResponse payment = ips.createPayment(orderId, money, payTypeEnum);
        Map<String, Object> map = new HashMap<>();
        map.put("codeUrl", payment.getCodeUrl());
        map.put("orderId", orderId);
        map.put("returnUrl", wxPayBean.getReturnUrl());
        return new ModelAndView("create", map);
    }

    /**
     * @param notifyData 接收到的支付平台的请求
     * @return 回送给支付平台的响应
     */

    @PostMapping("/notify")
    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData) {
        // TODO 通过MQ向电商模块回复消息
        return ips.asyncNotify(notifyData);
    }

    /**
     * @param orderId 关联的订单号
     * @return 支付记录
     */
    @GetMapping("/queryByOrderId")
    @ResponseBody
    public PayInfo queryByOrderNo(@RequestParam("orderId") String orderId) {
        PayInfo payInfo = ips.queryByOrderId(Long.parseLong(orderId));
        log.info("Ajax has queried, the pay is: {}", payInfo);
        return payInfo;
    }
}
