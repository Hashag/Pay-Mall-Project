package com.rikka.mall.controller;

import com.rikka.mall.service.ICategoryService;
import com.rikka.mall.vo.MallCategoryVO;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yuno
 * @time 3:27 PM 5/31/2023
 */
@RestController
@Slf4j
public class CategoryController {


    @Autowired
    private ICategoryService ics;

    /**
     * @return 所有目錄信息
     */
    @GetMapping("/categories")
    public ResponseVO<List<MallCategoryVO>> categoryQueryAll() {
        return ics.queryAll();
    }
}
