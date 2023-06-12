package com.rikka.mall.service;

import com.rikka.mall.form.CartAddForm;
import com.rikka.mall.form.CartUpdateForm;
import com.rikka.mall.pojo.CartProduct;
import com.rikka.mall.vo.CartVO;
import com.rikka.mall.vo.ResponseVO;

import java.util.List;

/**
 * @author Yuno
 * @time 6:47 PM 6/7/2023
 */
public interface ICartService {

    /**
     * 将一件商品累加到Redis中
     *
     * @param uid 用户id
     * @param caf 商品项简单信息
     * @return 购物车
     */
    ResponseVO<CartVO> add(Integer uid, CartAddForm caf);


    /**
     * 展示用户的购物车
     *
     * @param uid 用户id
     * @return 购物车
     */
    ResponseVO<CartVO> showAll(Integer uid);


    /**
     * 更新购物车商品项
     * @param uid 用户id
     * @param productId 商品id
     * @param form 商品项修改单
     * @return 购物车
     */
    ResponseVO<CartVO> update(Integer uid, Integer productId, CartUpdateForm form);

    /**
     * 删除购物车商品项
     * @param uid 用户id
     * @param productId 商品id
     * @return 购物车
     */
    ResponseVO<CartVO> delete(Integer uid, Integer productId);


    /**
     * 全选购物车商品项
     * @param uid 用户id
     * @return 购物车
     */
    ResponseVO<CartVO> selectAll(Integer uid);

    /**
     * 全不选购物车商品项
     * @param uid 用户id
     * @return 购物车
     */
    ResponseVO<CartVO> unSelectAll(Integer uid);

    /**
     * 购物车商品数量
     * @param uid 用户id
     * @return 购物车
     */
    ResponseVO<Integer> sum(Integer uid);

    /**
     * 查询简略信息
     * @param uid 用户id
     * @return 购物车
     */
    List<CartProduct> listForCart(Integer uid);
}
