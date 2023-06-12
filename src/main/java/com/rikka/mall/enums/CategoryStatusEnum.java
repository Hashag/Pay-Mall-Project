package com.rikka.mall.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @author Yuno
 * @time 1:34 PM 5/31/2023
 */
public enum CategoryStatusEnum {

    IN_USE(true, "使用中"),
    DESERT(false, "已棄用");
    private final boolean code;
    private final String desc;

    CategoryStatusEnum(boolean code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public boolean getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
