package com.rikka.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rikka.mall.dao.MallOrderItemMapper;
import com.rikka.mall.dao.MallOrderMapper;
import com.rikka.mall.dao.MallProductMapper;
import com.rikka.mall.dao.MallShippingMapper;
import com.rikka.mall.enums.OrderStatusEnum;
import com.rikka.mall.enums.PaymentTypeEnum;
import com.rikka.mall.enums.ProductStatusEnum;
import com.rikka.mall.enums.ResponseStatusEnum;
import com.rikka.mall.pojo.*;
import com.rikka.mall.service.ICartService;
import com.rikka.mall.service.IOrderService;
import com.rikka.mall.vo.CartVO;
import com.rikka.mall.vo.OrderItemVO;
import com.rikka.mall.vo.OrderVO;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Yuno
 * @time 7:38 AM 6/11/2023
 */

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {
    /**
     * 从Redis中获取购物车数据
     */
    @Autowired
    private ICartService ics;

    /**
     * 从MySQL中获取收货地址信息
     */
    @Autowired
    private MallShippingMapper msm;

    /**
     * 从MySQL中获取商品详细信息
     */
    @Autowired
    private MallProductMapper mpm;

    /**
     * 操作订单表
     */
    @Autowired
    private MallOrderMapper mom;

    /**
     * 操作订单详情表
     */
    @Autowired
    private MallOrderItemMapper moim;

    @Override
    @Transactional
    public ResponseVO<OrderVO> create(Integer uid, Integer shippingId) {

        /*
        1. 地址校验
        2. 购物车校验
        3. 构造订单字段
        4. 使用事务写入到数据库
        5. 减库存
        6. 更新购物车
        7. 回显订单数据
         ...
         */

        // 1. 收货地址校验
        MallShippingExample mse = new MallShippingExample();
        mse.createCriteria().andIdEqualTo(shippingId).andUserIdEqualTo(uid);
        List<MallShipping> mallShippings = msm.selectByExample(mse);
        if (mallShippings.isEmpty()) return ResponseVO.error(ResponseStatusEnum.SHIPPING_NOT_EXIST);
        log.info("the address is: {}", mallShippings.get(0));

        // 2. 购物车校验
        List<CartProduct> cartProducts = ics.listForCart(uid);
        // 过滤没选中的商品
        List<CartProduct> cartSelected = cartProducts.stream().filter(CartProduct::getProductSelected).collect(Collectors.toList());
        List<Integer> productIds = cartSelected.stream().map(CartProduct::getProductId).collect(Collectors.toList());
        if (productIds.isEmpty()) return ResponseVO.error(ResponseStatusEnum.EMPTY_CART);
        MallProductExample mpe = new MallProductExample();
        mpe.createCriteria().andIdIn(productIds);
        List<MallProduct> products = mpm.selectByExample(mpe);
        log.info("the products is: {}", products);
        Map<Integer, MallProduct> pMap = products.stream().collect(Collectors.toMap(MallProduct::getId, e -> e));

        // insertItems 存放要插入的订单项
        List<MallOrderItem> insertItems = new ArrayList<>(cartSelected.size());
        // 订单中商品的总金额
        BigDecimal totalProductPrice = BigDecimal.ZERO;
        Long orderNo = generateOrderNo();
        for (CartProduct cp : cartSelected) {
            // 检查库存, 商品状态
            MallProduct mp = pMap.get(cp.getProductId());
            if (mp == null)
                return ResponseVO.error(ResponseStatusEnum.PRODUCT_NOT_EXIST, "商品id: " + cp.getProductId() + "不存在");
            if (ProductStatusEnum.TAKE_OFF.getCode().equals(mp.getStatus()) || ProductStatusEnum.DELETE.getCode().equals(mp.getStatus()))
                return ResponseVO.error(ResponseStatusEnum.PRODUCT_OFF_SALE_OR_DELETE);
            if (cp.getQuantity() > mp.getStock())
                return ResponseVO.error(ResponseStatusEnum.PRODUCT_STOCK_ERROR, "库存不足");

            // 构造订单详情记录
            MallOrderItem mallOrderItem = generateOrderItem(uid, orderNo, cp.getProductId(), mp.getName(), mp.getMainImage(), mp.getPrice(), cp.getQuantity());
            insertItems.add(mallOrderItem);
            totalProductPrice = totalProductPrice.add(mallOrderItem.getTotalPrice());
        }
        // 3 & 4 构造记录 & 基于事务写入到数据库
        // 构造订单记录: 运费默认是0
        MallOrder mallOrder = generateOrder(orderNo, uid, shippingId, totalProductPrice, PaymentTypeEnum.ONLINE_PAY, BigDecimal.ZERO, OrderStatusEnum.NOT_PAY);

        int rowOrder = mom.insertSelective(mallOrder);
        if (rowOrder != 1) {
            log.error("在尝试生成用户 {} 的订单 {} 时, 数据库错误", uid, orderNo);
            throw new RuntimeException("数据库错误");
        }
        log.info("Table mall_order {} rows inserted", rowOrder);
        int rowItems = moim.insertBatches(insertItems);
        if (rowItems != insertItems.size()) {
            log.error("在尝试生成用户 {} 的订单 {} 时, 数据库错误", uid, orderNo);
            throw new RuntimeException("数据库错误");
        }
        log.info("Table mall_order_item {} rows inserted", rowItems);

        // 5. 减少数据库中商品的库存
        for (CartProduct cp : cartSelected) {
            MallProduct mp = pMap.get(cp.getProductId());
            mp.setStock(mp.getStock() - cp.getQuantity());
            int row = mpm.updateByPrimaryKeySelective(mp);
            if (row != 1) {
                log.error("在尝试更新库存时, 数据库错误; 用户id: {}, 订单号: {}", uid, orderNo);
                throw new RuntimeException("数据库错误");
            }
        }
        // 6. 更新购物车
        for (CartProduct cp : cartSelected) {
            ResponseVO<CartVO> delete = ics.delete(uid, cp.getProductId());
            if (!ResponseStatusEnum.SUCCESS.getCode().equals(delete.getStatus())) {
                log.error("在尝试更新购物车时, 数据库错误; 用户id: {}, 订单号: {}", uid, orderNo);
                throw new RuntimeException("数据库错误");
            }
        }

        // 7. 返回响应结果
        List<OrderItemVO> orderItemVOS = generateOrderItemVO(insertItems);
        OrderVO orderVO = generateOrderVO(mallOrder, mallShippings.get(0), orderItemVOS);

        return ResponseVO.success(orderVO);
    }

    /**
     * 构造订单结果响应
     */
    private OrderVO generateOrderVO(MallOrder mallOrder, MallShipping mallShipping, List<OrderItemVO> voList) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(mallOrder, orderVO);
        orderVO.setShippingVo(mallShipping);
        orderVO.setOrderItemVoList(voList);
        return orderVO;
    }

    /**
     * 构造订单详情响应
     */
    private List<OrderItemVO> generateOrderItemVO(List<MallOrderItem> insertItems) {
        List<OrderItemVO> ans = new ArrayList<>(insertItems.size());
        for (MallOrderItem insertItem : insertItems) {
            OrderItemVO oi = new OrderItemVO();
            BeanUtils.copyProperties(insertItem, oi);
            ans.add(oi);
        }

        return ans;
    }

    /**
     * 构造一条订单记录
     */
    private MallOrder generateOrder(Long orderNo, Integer userId, Integer shippingId, BigDecimal productPrice, PaymentTypeEnum paymentType, BigDecimal postage, OrderStatusEnum status) {
        MallOrder mo = new MallOrder();
        mo.setOrderNo(orderNo);
        mo.setUserId(userId);
        mo.setShippingId(shippingId);
        // 实际付款金额是 商品总价 + 运费
        mo.setPayment(productPrice.add(postage));
        mo.setPaymentType(paymentType.getCode());
        mo.setPostage(postage);
        mo.setStatus(status.getCode());

        return mo;
    }

    /**
     * 构造一条订单详情记录
     */
    private MallOrderItem generateOrderItem(Integer userId, Long orderNo, Integer productId, String productName, String productImag, BigDecimal currentUnitPrice, Integer quantity) {
        MallOrderItem moi = new MallOrderItem();
        moi.setUserId(userId);
        moi.setOrderNo(orderNo);
        moi.setProductId(productId);
        moi.setProductName(productName);
        moi.setProductImage(productImag);
        moi.setCurrentUnitPrice(currentUnitPrice);
        moi.setQuantity(quantity);
        moi.setTotalPrice(BigDecimal.valueOf(quantity).multiply(currentUnitPrice));

        return moi;
    }

    /**
     * 构造订单号: 此处以时间戳作为订单号; 更为高级的: 详见分布式唯一键生成策略
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis();
    }


    @Override
    public ResponseVO<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        MallOrderExample moe = new MallOrderExample();
        moe.createCriteria().andUserIdEqualTo(uid);
        List<MallOrder> mallOrders = mom.selectByExample(moe);
        if (mallOrders.isEmpty()) {
            // 没有订单, 直接返回空的分页响应
            return ResponseVO.success(new PageInfo(mallOrders));
        }

        // 订单详情
        Set<Long> orderNoSet = mallOrders.stream().map(MallOrder::getOrderNo).collect(Collectors.toSet());
        MallOrderItemExample moie = new MallOrderItemExample();
        moie.createCriteria().andOrderNoIn(new ArrayList<>(orderNoSet));
        List<MallOrderItem> mallOrderItems = moim.selectByExample(moie);
        Map<Long, List<OrderItemVO>> itemGroup = mallOrderItems.stream().map(e -> {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(e, orderItemVO);
            return orderItemVO;
        }).collect(Collectors.groupingBy(OrderItemVO::getOrderNo));

        // 收货地址
        Set<Integer> shippingSet = mallOrders.stream().map(MallOrder::getShippingId).collect(Collectors.toSet());
        MallShippingExample mse = new MallShippingExample();
        mse.createCriteria().andIdIn(new ArrayList<>(shippingSet));
        List<MallShipping> mallShippings = msm.selectByExample(mse);
        Map<Integer, MallShipping> shippingMap = mallShippings.stream().collect(Collectors.toMap(MallShipping::getId, e -> e));

        // 设置分页信息
        PageInfo pageInfo = new PageInfo(mallOrders);
        List<OrderVO> res = mallOrders.stream().map(e ->
                generateOrderVO(e, shippingMap.get(e.getShippingId()), itemGroup.get(e.getOrderNo())
                )).collect(Collectors.toList());
        pageInfo.setList(res);
        return ResponseVO.success(pageInfo);
    }


    /**
     * @param uid     用户id
     * @param orderNo 订单号 订单号是唯一键
     * @return {@inheritDoc}
     */
    @Override
    public ResponseVO<OrderVO> detail(Integer uid, Long orderNo) {
        MallOrderExample moe = new MallOrderExample();
        moe.createCriteria().andOrderNoEqualTo(orderNo).andUserIdEqualTo(uid);
        List<MallOrder> mallOrders = mom.selectByExample(moe);
        if (mallOrders.isEmpty()) return ResponseVO.error(ResponseStatusEnum.ORDER_NOT_EXIST);

        MallOrder mallOrder = mallOrders.get(0);
        // 地址
        MallShipping mallShipping = msm.selectByPrimaryKey(mallOrder.getShippingId());
        // 订单详情
        MallOrderItemExample moie = new MallOrderItemExample();
        moie.createCriteria().andOrderNoEqualTo(mallOrder.getOrderNo());
        List<MallOrderItem> mallOrderItems = moim.selectByExample(moie);
        // 构造响应
        List<OrderItemVO> orderItemVOS = generateOrderItemVO(mallOrderItems);
        OrderVO ans = generateOrderVO(mallOrder, mallShipping, orderItemVOS);

        return ResponseVO.success(ans);
    }


    @Override
    public ResponseVO<Void> cancel(Integer uid, Long orderNo) {
        MallOrderExample moe = new MallOrderExample();
        moe.createCriteria().andOrderNoEqualTo(orderNo).andUserIdEqualTo(uid);
        List<MallOrder> mallOrders = mom.selectByExample(moe);
        if (mallOrders.isEmpty()) return ResponseVO.error(ResponseStatusEnum.ORDER_NOT_EXIST);

        MallOrder mallOrder = mallOrders.get(0);
        // 只有在 [未支付] 的状态下才能取消订单
        if (!OrderStatusEnum.NOT_PAY.getCode().equals(mallOrder.getStatus())) {
            return ResponseVO.error(ResponseStatusEnum.ORDER_STATUS_ERROR, "取消订单失败");
        }
        mallOrder.setStatus(OrderStatusEnum.CANCELED.getCode());
        mallOrder.setCloseTime(new Date());
        // 更新时间由数据库自己管理
        mallOrder.setUpdateTime(null);
        int update = mom.updateByPrimaryKeySelective(mallOrder);
        if (update != 1) return ResponseVO.error(ResponseStatusEnum.SERVER_ERROR);

        return ResponseVO.success("成功取消");
    }

    @Override
    public void paid(Long orderNo) {
        /*
         TODO 校验订单金额和支付金额是否一致
                可以在支付跳转的时候查询订单的数据, 以校验支付记录和订单记录
         */

        MallOrderExample mallOrderExample = new MallOrderExample();
        mallOrderExample.createCriteria().andOrderNoEqualTo(orderNo);
        List<MallOrder> mallOrders = mom.selectByExample(mallOrderExample);
        if (mallOrders.isEmpty()) throw new RuntimeException("数据库内部数据异常, 支付响应失败, 订单号: " + orderNo);

        MallOrder mallOrder = mallOrders.get(0);
        if (OrderStatusEnum.NOT_PAY.getCode().equals(mallOrder.getStatus())) {
            // 更改订单状态
            mallOrder.setStatus(OrderStatusEnum.PAID.getCode());
            mallOrder.setPaymentTime(new Date());
            mallOrder.setUpdateTime(null);

            int row = mom.updateByExampleSelective(mallOrder, mallOrderExample);
            if (row != 1) throw new RuntimeException("数据库内部错误, 更新订单号: " + orderNo + "状态失败");
            log.info("支付成功~~~, 订单号为: {}", orderNo);
        }

    }
}
