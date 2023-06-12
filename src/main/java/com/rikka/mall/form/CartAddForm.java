package com.rikka.mall.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 用于购物车添加/移除商品功能
 * @author Yuno
 * @time 3:22 PM 6/7/2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartAddForm {

    @NotNull(message = "商品id为空")
    private Integer productId;
    // TODO 此字段的作用, 直接为true不行吗?
    private Boolean selected = true;
}
