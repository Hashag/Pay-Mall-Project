package com.rikka.mall.service.impl;

import com.github.pagehelper.PageInfo;
import com.rikka.mall.MallApplicationTests;
import com.rikka.mall.enums.ResponseStatusEnum;
import com.rikka.mall.form.ShippingForm;
import com.rikka.mall.service.IShippingService;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yuno
 * @time 10:12 AM 6/10/2023
 */

@Slf4j
@DisplayName("收货模块")
@Transactional
class ShippingServiceImplTest extends MallApplicationTests {

    @Autowired
    private IShippingService iss;

    ShippingForm sf = new ShippingForm(
            "yuno",
            "123",
            "456",
            "TK",
            "Tokyo",
            "JP",
            "JP",
            "#FFFF");
    Integer uid = 1;
    Integer shippingId = 6;

    @Test
    @DisplayName("添加收货地址")
    void add() {
        ResponseVO<Map<String, Integer>> add = iss.add(uid, sf);

        log.info("the add result is: {}", gson.toJson(add));
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), add.getStatus());
    }

    @Test
    @DisplayName("删除收货地址")
    void delete() {
        ResponseVO<Void> delete = iss.delete(uid, shippingId);

        log.info("the delete is: {}", gson.toJson(delete));
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), delete.getStatus());
    }

    @Test
    @DisplayName("更新收货地址")
    void update() {
        ShippingForm form = new ShippingForm();
        form.setReceiverName("Gasai Yuno");
        ResponseVO<Void> update = iss.update(uid, shippingId, form);

        log.info("the update is: {}", gson.toJson(update));
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), update.getStatus());
    }

    @Test
    @DisplayName("列出用户的收货地址")
    void list() {
        ResponseVO<PageInfo> list = iss.list(uid, 1, 10);

        log.info("the all shipping info is: {}", gson.toJson(list));
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), list.getStatus());
    }
}