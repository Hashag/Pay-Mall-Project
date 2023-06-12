package com.rikka.mall.enums;

import lombok.Getter;

/**
 * @author Yuno
 * @time 3:39 PM 5/30/2023
 */
@Getter
public enum UserRoleEnum {
    ADMIN(0),
    NORMAL(1);
    private final Integer code;

    UserRoleEnum(int code) {
        this.code = code;
    }
}
