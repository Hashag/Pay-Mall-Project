package com.rikka.pay.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("wx")
public class WXPayBean {

    private String appId;
    private String mchId;
    private String mchKey;
    private String notifyUrl;
    private String returnUrl;

}
