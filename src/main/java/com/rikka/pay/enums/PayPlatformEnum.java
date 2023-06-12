package com.rikka.pay.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Getter;

@Getter
public enum PayPlatformEnum {

    ALIPAY(1),
    WX(2);
    private final Integer code;

    PayPlatformEnum(int code) {
        this.code = code;
    }

    /**
     *
     * @param bestPayTypeEnum 外部指定支付类型
     * @return 创建支付单所需的支付平台
     */
    public static PayPlatformEnum getPlatform(BestPayTypeEnum bestPayTypeEnum) {
        for (PayPlatformEnum value : values()) {
            if (value.name().equals(bestPayTypeEnum.getPlatform().name())) {
                return value;
            }
        }
        throw new IllegalArgumentException("未知的支付方式");
    }

}
