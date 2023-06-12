package com.rikka.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *
 * 此类用于商品信息的简单展示, 因此不包括其他详细信息
 * @author Yuno
 * @time 3:12 PM 6/6/2023
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MallProductVO {

    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private Integer status;

    private BigDecimal price;
}
