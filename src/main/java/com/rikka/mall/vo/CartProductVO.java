package com.rikka.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *表示购物车中的商品项
 * @author Yuno
 * @time 4:26 PM 6/7/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProductVO {

    private Integer productId;

    /**
     * 购买的数量
     */
    private Integer quantity;

    private String productName;

    private String productSubtitle;

    private String productMainImage;

    private BigDecimal productPrice;

    /**
     * 产品状态: 开始时, 用户购物车的产品是在售状态, 之后变为了下架状态
     */
    private Integer productStatus;

    /**
     * 等于 quantity * productPrice
     */
    private BigDecimal productTotalPrice;

    private Integer productStock;

    /**
     * 商品是否选中:  TODO 指的是商品在前端页面的勾选状态吗?
     */
    private Boolean productSelected;
}
