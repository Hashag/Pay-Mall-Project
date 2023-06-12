package com.rikka.pay.config;


import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayConfig {

    @Autowired
    private WXPayBean wxPayBean;

    @Bean
    public BestPayServiceImpl bestPayService() {
        WxPayConfig wpc = new WxPayConfig();
        wpc.setAppId(wxPayBean.getAppId());
        wpc.setMchId(wxPayBean.getMchId());
        wpc.setMchKey(wxPayBean.getMchKey());
        wpc.setNotifyUrl(wxPayBean.getNotifyUrl());
        wpc.setReturnUrl(wxPayBean.getReturnUrl());
        //支付类, 所有方法都在这个类里
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wpc);
        return bestPayService;
    }


}
