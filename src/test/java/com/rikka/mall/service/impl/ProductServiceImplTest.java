package com.rikka.mall.service.impl;

import com.github.pagehelper.PageInfo;
import com.rikka.mall.MallApplicationTests;
import com.rikka.mall.enums.ResponseStatusEnum;
import com.rikka.mall.service.IProductService;
import com.rikka.mall.vo.MallProductDetailVO;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yuno
 * @time 3:27 PM 6/6/2023
 */

@Slf4j
@DisplayName("商品模块")
class ProductServiceImplTest extends MallApplicationTests {

    @Autowired
    private IProductService ips;

    Integer cid = 0;
    Integer pageNum = 2;
    Integer pageSize = 1;
    Integer pid = 26;

    @Test
    @DisplayName("按类目id分页查询")
    void queryByCategoryId() {
        ResponseVO<PageInfo> productVOS = ips.queryByCategoryId(cid, pageNum, pageSize);
        log.info("productVOS is: {}", gson.toJson(productVOS));
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), productVOS.getStatus());
    }

    @Test
    @DisplayName("商品详细信息")
    void queryById() {
        ResponseVO<MallProductDetailVO> res = ips.queryById(pid);
        log.info("the detail product is: {}", gson.toJson(res));
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), res.getStatus());
    }

}