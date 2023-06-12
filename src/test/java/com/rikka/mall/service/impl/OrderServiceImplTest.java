package com.rikka.mall.service.impl;

import com.github.pagehelper.PageInfo;
import com.rikka.mall.MallApplicationTests;
import com.rikka.mall.enums.ResponseStatusEnum;
import com.rikka.mall.form.CartAddForm;
import com.rikka.mall.service.ICartService;
import com.rikka.mall.service.IOrderService;
import com.rikka.mall.vo.CartVO;
import com.rikka.mall.vo.OrderVO;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Yuno
 * @time 7:41 AM 6/11/2023
 */

@Slf4j
@DisplayName("订单模块")
class OrderServiceImplTest extends MallApplicationTests {

    @Autowired
    private IOrderService ios;

    @Autowired
    private ICartService ics;
    Integer uid = 1;
    Integer shippingId = 6;

    @Nested
    @DisplayName("内部创建订单测试")
    class Add extends MallApplicationTests {
        @BeforeEach
        @DisplayName("添加")
        void add() {
            Integer uid = 1;
            Integer pid = 26;
            Integer pid2 = 27;
            Boolean selected = true;
            ResponseVO<CartVO> add = ics.add(uid, new CartAddForm(pid, selected));
            ResponseVO<CartVO> add2 = ics.add(uid, new CartAddForm(pid2, selected));

            assertEquals(ResponseStatusEnum.SUCCESS.getCode(), add.getStatus());
            assertEquals(ResponseStatusEnum.SUCCESS.getCode(), add2.getStatus());
            log.info("test the add is: {}", gson.toJson(add));
            log.info("test the add is: {}", gson.toJson(add2));
        }

        @Test
        @DisplayName("创建订单")
        void create() {
            ResponseVO<OrderVO> orderVOResponseVO = ios.create(uid, shippingId);

            log.info("orderVOResponseVO is: {}", gson.toJson(orderVOResponseVO));
            assertEquals(ResponseStatusEnum.SUCCESS.getCode(), orderVOResponseVO.getStatus());
        }
    }


    @Test
    @DisplayName("查询用户的所有订单")
    void list() {
        ResponseVO<PageInfo> list = ios.list(uid, 1, 1);

        log.info("the list is: {}", gson.toJson(list));
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), list.getStatus());
    }

    @Test
    @DisplayName("查询某个订单")
    void detail() {
        Long orderNo = 1686477136580L;
        ResponseVO<OrderVO> detail = ios.detail(uid, orderNo);

        log.info("the detail is: {}", gson.toJson(detail));
        //assertEquals(ResponseStatusEnum.SUCCESS.getCode(), detail.getStatus());
    }

    @Test
    @DisplayName("取消某个未支付的订单")
    void cancel() {
        Long orderNo = 1686477136580L;
        ResponseVO<Void> cancel = ios.cancel(uid, orderNo);
        log.info("the cancel is: {}", gson.toJson(cancel));

        //assertEquals(ResponseStatusEnum.SUCCESS.getCode(), cancel.getStatus());
    }

    @Test
    void paid() {
    }
}