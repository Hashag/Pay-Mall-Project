package com.rikka.mall.service;

import com.rikka.mall.vo.MallCategoryVO;
import com.rikka.mall.vo.ResponseVO;

import java.util.List;
import java.util.Set;

/**
 * @author Yuno
 * @time 11:39 AM 5/31/2023
 */
public interface ICategoryService {

    /**
     *
     * @return 所有的目錄信息
     */
    ResponseVO<List<MallCategoryVO>> queryAll();


    /**
     *  TODO 如何定义给定cId为null 时的查询行为 ?
     * 如果cId为null, 那么将返回一个空集合
     * @param cId 类目id
     * @return 类目为cId及其所有子类目的id集合
     */
    Set<Integer>  queryByCategoryId(Integer cId);
}
