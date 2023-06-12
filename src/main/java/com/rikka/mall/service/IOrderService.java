package com.rikka.mall.service;

import com.github.pagehelper.PageInfo;
import com.rikka.mall.vo.OrderVO;
import com.rikka.mall.vo.ResponseVO;

/**
 * @author Yuno
 * @time 7:32 AM 6/11/2023
 */
public interface IOrderService {


    /**
     *        生成订单信息
     *         1. 地址校验
     *         2. 购物车校验
     *         3. 构造订单字段
     *         4. 使用事务写入到数据库
     *         5. 减库存
     *         6. 更新购物车
     *         7. 回显订单数据
     * @param uid 用户id
     * @param shippingId 地址id
     * @return 订单生成的结果
     */
    ResponseVO<OrderVO> create(Integer uid, Integer shippingId);

    /**
     * 对查询到的订单记录执行分页
     * @param uid 用户id
     * @param pageNum 页号
     * @param pageSize 页大小
     * @return 订单分页信息
     */
    ResponseVO<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);

    /**
     * 单独查询某个订单
     * @param uid 用户id
     * @param orderNo 订单号
     * @return 订单详情信息
     */
    ResponseVO<OrderVO> detail(Integer uid, Long orderNo);

    /**
     * 取消订单
     * @param uid 用户id
     * @param orderNo 订单号
     * @return 操作结果
     */
    ResponseVO<Void> cancel(Integer uid, Long orderNo);

    /**
     * 更改订单状态
     * @param orderNo 订单号
     */
    void paid(Long orderNo);
}
