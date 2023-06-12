package com.rikka.mall.enums;

import lombok.Getter;

/**
 * @author Yuno
 * @time 12:24 PM 6/11/2023
 */
@Getter
public enum PaymentTypeEnum {

    ONLINE_PAY(1, "在线支付");

    private final Integer code;
    private final String desc;

    PaymentTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
