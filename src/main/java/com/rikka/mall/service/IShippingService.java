package com.rikka.mall.service;

import com.github.pagehelper.PageInfo;
import com.rikka.mall.form.ShippingForm;
import com.rikka.mall.vo.ResponseVO;

import java.util.Map;

/**
 * @author Yuno
 * @time 9:59 AM 6/10/2023
 */
public interface IShippingService {


    /**
     *添加收货地址表单
     * @param uid 用户id
     * @param form 收获地址表单
     * @return 返回的表单id
     */
    ResponseVO<Map<String, Integer>> add(Integer uid, ShippingForm form);


    /**
     *删除收货地址记录, 使用uid做安全校验
     * @param uid 用户id
     * @param shippingId 收货地址表单id
     * @return 删除结果
     */
    ResponseVO<Void> delete(Integer uid, Integer shippingId);


    /**
     *删除收货地址记录, 使用uid做安全校验
     * @param uid 用户id
     * @param shippingId 收货地址表单id
     * @param form 待更新的内容
     * @return 更新结果
     */
    ResponseVO<Void> update(Integer uid, Integer shippingId, ShippingForm form);

    /**
     *
     * @param uid 用户id
     * @param pageNum 页号
     * @param pageSize 页大小
     * @return 分页信息
     */
    ResponseVO<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);
}
