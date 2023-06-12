package com.rikka.mall.service.impl;

import com.rikka.mall.MallApplicationTests;
import com.rikka.mall.enums.ResponseStatusEnum;
import com.rikka.mall.service.ICategoryService;
import com.rikka.mall.vo.MallCategoryVO;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yuno
 * @time 11:55 AM 5/31/2023
 */
@Slf4j
@DisplayName("类目模块")
class CategoryServiceImplTest extends MallApplicationTests {

    @Autowired
    private ICategoryService ics;

    Integer cid = 100001;

    @Test
    @DisplayName("所有类目信息")
    void queryAll() {
        ResponseVO<List<MallCategoryVO>> listResponseVO = ics.queryAll();
        log.info("all category is: {}", gson.toJson(listResponseVO));
        assertEquals(ResponseStatusEnum.SUCCESS.getCode(), listResponseVO.getStatus());
    }

    @Test
    @DisplayName("查询同类下的所有类目id")
    void queryByCategoryId() {
        Set<Integer> set = ics.queryByCategoryId(cid);
        log.info("the set is: {}", gson.toJson(set));
        assertNotNull(set);
    }

}