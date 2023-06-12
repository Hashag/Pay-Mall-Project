package com.rikka.mall.enums;

import lombok.Getter;

/**
 * @author Yuno
 * @time 12:28 PM 6/11/2023
 */
@Getter
public enum OrderStatusEnum {

    /**
     * 订单状态: 0 -> 已取消 10 ->未付款  20 -> 已付款  40 -> 已发货 50 -> 交易成功 60 -> 交易关闭
     */
    CANCELED(0, "已取消"),
    NOT_PAY(10, "未付款"),
    PAID(20, "已付款"),
    SHIPPED(40, "已发货"),
    TRADE_SUCCESS(50, "交易成功"),
    TRADE_CLOSED(60, "交易关闭");

    private final Integer code;
    private final String desc;

    OrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
