package com.rikka.mall.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车商品项对象和Redis进行交互
 * @author Yuno
 * @time 7:11 PM 6/7/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {

    private Integer productId;

    private Integer quantity;

    private Boolean productSelected;
}
