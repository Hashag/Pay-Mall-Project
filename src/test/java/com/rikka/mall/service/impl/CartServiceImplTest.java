package com.rikka.mall.service.impl;

import com.rikka.mall.MallApplicationTests;
import com.rikka.mall.enums.ResponseStatusEnum;
import com.rikka.mall.form.CartAddForm;
import com.rikka.mall.form.CartUpdateForm;
import com.rikka.mall.pojo.CartProduct;
import com.rikka.mall.service.ICartService;
import com.rikka.mall.vo.CartVO;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yuno
 * @time 7:25 PM 6/7/2023
 */

@Slf4j
@DisplayName("购物车模块")
class CartServiceImplTest extends MallApplicationTests {

    @Autowired
    private ICartService ics;

    Integer uid = 1;
    Integer pid = 26;
    Integer pid2 = 27;
    Boolean selected = true;


    @DisplayName("添加到购物车")
    @BeforeEach
    void add() {
        ResponseVO<CartVO> add = ics.add(uid, new CartAddForm(pid, selected));
        ResponseVO<CartVO> add2 = ics.add(uid, new CartAddForm(pid2, selected));

        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), add.getStatus());
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), add2.getStatus());
        log.info("test the add is: {}", gson.toJson(add));
        log.info("test the add is: {}", gson.toJson(add2));
    }

    @Test
    @DisplayName("展示用户的购物车")
    void showAll() {
        ResponseVO<CartVO> showAll = ics.showAll(1);

        log.info("the json is: {}", gson.toJson(showAll));
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), showAll.getStatus());
    }


    @Test
    @DisplayName("更新购物车")
    void update() {
        CartUpdateForm cuf = new CartUpdateForm(10, true);
        ResponseVO<CartVO> update = ics.update(uid, pid, cuf);

        log.info("the json is: {}", gson.toJson(update));
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), update.getStatus());
    }

    @DisplayName("从购物车中删除")
    @AfterEach
    void delete() {
        ResponseVO<CartVO> delete = ics.delete(uid, pid);
        ResponseVO<CartVO> delete2 = ics.delete(uid, pid2);

        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), delete.getStatus());
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), delete2.getStatus());
        log.info("the json is: {}", gson.toJson(delete));
        log.info("the json is: {}", gson.toJson(delete2));
    }

    @Test
    @DisplayName("全选中购物车商品")
    void selectAll() {
        ResponseVO<CartVO> selectAll = ics.selectAll(uid);

        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), selectAll.getStatus());
        log.info("the json is: {}", gson.toJson(selectAll));
    }

    @Test
    @DisplayName("全部取消选中购物车商品")
    void unSelectAll() {
        ResponseVO<CartVO> unSelectAll = ics.unSelectAll(uid);

        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), unSelectAll.getStatus());
        log.info("the json is: {}", gson.toJson(unSelectAll));
    }

    @Test
    @DisplayName("购物车中商品个数")
    void sum() {
        ResponseVO<Integer> sum = ics.sum(uid);

        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), sum.getStatus());
        log.info("the json is: {}", gson.toJson(sum));
    }

    @Test
    @DisplayName("展示购物车简略信息")
    void listForCart() {
        List<CartProduct> cartProducts = ics.listForCart(uid);

        assertNotNull(cartProducts);
        log.info("the json is: {}", gson.toJson(cartProducts));
    }
}