package com.rikka.mall.controller;

import com.github.pagehelper.PageInfo;
import com.rikka.mall.service.IProductService;
import com.rikka.mall.vo.MallProductDetailVO;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yuno
 * @time 5:37 PM 6/6/2023
 */

@RestController
@Slf4j
public class ProductController {


    @Autowired
    private IProductService ips;


    /**
     * 通过商品id查询详细信息
     * @param pid 商品id
     * @return 商品详细信息
     */
    @GetMapping("/products/{productId}")
    public ResponseVO<MallProductDetailVO> productDetail(@PathVariable("productId") Integer pid) {
        return ips.queryById(pid);
    }

    /**
     * 若categoryId不存在, 则返回所有类目的商品信息
     *
     * @param categoryId 类目id
     * @param pageNum    页号
     * @param pageSize   页面内容数量
     * @return 分页具体内容
     */
    @GetMapping("/products")
    public ResponseVO<PageInfo> queryByCategoryId(@RequestParam(required = false) Integer categoryId,
                                                  @RequestParam(defaultValue = "1") Integer pageNum,
                                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        return ips.queryByCategoryId(categoryId, pageNum, pageSize);
    }
}
