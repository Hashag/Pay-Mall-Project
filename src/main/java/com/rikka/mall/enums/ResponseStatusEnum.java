package com.rikka.mall.enums;

import lombok.Getter;

/**
 * @author Yuno
 * @time 5:17 PM 5/30/2023
 */

@Getter
public enum ResponseStatusEnum {

    SERVER_ERROR(-1, "服务器错误"),
    SUCCESS(0, "成功"),
    PASSWD_WRONG(1, "密码错误"),
    USER_EXISTED(2, "用户已存在"),
    PARAM_ERROR(3, "参数错误"),
    EMAIL_BOUND(4, "邮箱已绑定"),
    NEED_LOGIN(10, "未登录"),
    USERNAME_OR_PASSWD_ERROR(11, "用戶名或密碼錯誤"),
    PRODUCT_OFF_SALE_OR_DELETE(12, "商品下架或删除"),
    PRODUCT_NOT_EXIST(13, "商品不存在"),
    PRODUCT_STOCK_ERROR(14, "库存不正确"),
    CART_PRODUCT_NOT_EXIST(15, "购物车里无此商品"),
    DELETE_SHIPPING_FAIL(16, "删除收货地址失败"),
    SHIPPING_NOT_EXIST(17, "收货地址不存在"),
    CART_SELECTED_IS_EMPTY(18, "请选择商品后下单"),
    ORDER_NOT_EXIST(19, "订单不存在"),
    ORDER_STATUS_ERROR(20, "订单状态有误"),
    EMPTY_CART(21, "购物车为空");

    /**
     * -1 - 服务端错误
     * 0  - 成功
     * 1  - 密码错误
     * 2  - 用户存在
     * 10 - 用户未登录
     */
    private final Integer code;
    private final String desc;

    ResponseStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
