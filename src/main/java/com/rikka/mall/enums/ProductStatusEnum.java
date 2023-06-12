package com.rikka.mall.enums;

import lombok.Getter;

/**
 * @author Yuno
 * @time 9:47 PM 6/6/2023
 */

@Getter
public enum ProductStatusEnum {
    IN_STOKE(1, "在售"),
    TAKE_OFF(2, "下架"),
    DELETE(3, "删除");

    private final Integer code;
    private final String dec;

    ProductStatusEnum(Integer code, String dec) {
        this.code = code;
        this.dec = dec;
    }

}
