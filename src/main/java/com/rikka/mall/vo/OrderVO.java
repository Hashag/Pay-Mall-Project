package com.rikka.mall.vo;

import com.rikka.mall.pojo.MallShipping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Yuno
 * @time 7:26 AM 6/11/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO {


    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    /**
     * 邮费
     */
    private Integer postage;

    /**
     * 订单状态: 0 -> 已取消 10 ->未付款  20 -> 已付款  40 -> 已发货 50 -> 交易成功 60 -> 交易关闭
     */
    private Integer status;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;

    private List<OrderItemVO> orderItemVoList;

    private Integer shippingId;

    private MallShipping shippingVo;
}
