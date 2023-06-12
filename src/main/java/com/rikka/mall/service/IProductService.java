package com.rikka.mall.service;

import com.github.pagehelper.PageInfo;
import com.rikka.mall.vo.MallProductDetailVO;
import com.rikka.mall.vo.MallProductVO;
import com.rikka.mall.vo.ResponseVO;

import java.util.List;

/**
 * @author Yuno
 * @time 3:16 PM 6/6/2023
 */
public interface IProductService {

    /**
     * 如果cId为null, 就返回所有类目的商品信息
     *
     * @param cId      商品目录id
     * @param pageNo   页号
     * @param pageSize 页大小
     * @return 属于该目录及其子目录的所哟商品信息
     */
    ResponseVO<PageInfo> queryByCategoryId(Integer cId, Integer pageNo, Integer pageSize);


    /**
     * 通过主键, 查询商品的详细信息
     *
     * @param pid 商品主键
     * @return 商品详细信息
     */
    ResponseVO<MallProductDetailVO> queryById(Integer pid);
}
