package com.rikka.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车
 *
 * @author Yuno
 * @time 6:45 PM 6/7/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartVO {

    private List<CartProductVO> cartProductVoList;

    private Boolean selectedAll;

    /**
     * 只针对选中的商品计算价格
     */
    private BigDecimal cartTotalPrice;

    private Integer cartTotalQuantity;
}
