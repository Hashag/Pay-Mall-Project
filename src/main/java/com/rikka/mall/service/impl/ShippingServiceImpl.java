package com.rikka.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rikka.mall.dao.MallShippingMapper;
import com.rikka.mall.enums.ResponseStatusEnum;
import com.rikka.mall.form.ShippingForm;
import com.rikka.mall.pojo.MallShipping;
import com.rikka.mall.pojo.MallShippingExample;
import com.rikka.mall.service.IShippingService;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yuno
 * @time 10:01 AM 6/10/2023
 */

@Service
@Slf4j
public class ShippingServiceImpl implements IShippingService {


    @Autowired
    private MallShippingMapper msm;

    /**
     * @param uid  {@inheritDoc}
     * @param form {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ResponseVO<Map<String, Integer>> add(Integer uid, ShippingForm form) {
        MallShipping ms = new MallShipping();
        BeanUtils.copyProperties(form, ms);
        ms.setUserId(uid);
        int row = msm.insertSelective(ms);
        if (row == 0) return ResponseVO.error(ResponseStatusEnum.SERVER_ERROR);

        log.info("the inserted ms is: {}", ms);
        Map<String, Integer> map = new HashMap<>();
        map.put("shippingId", ms.getId());
        return ResponseVO.success(map, "添加成功");
    }

    @Override
    public ResponseVO<Void> delete(Integer uid, Integer shippingId) {
        MallShippingExample mse = new MallShippingExample();
        mse.createCriteria().andIdEqualTo(shippingId).andUserIdEqualTo(uid);
        int row = msm.deleteByExample(mse);
        if (row == 0) return ResponseVO.error(ResponseStatusEnum.SHIPPING_NOT_EXIST);
        return ResponseVO.success("删除成功");
    }

    @Override
    public ResponseVO<Void> update(Integer uid, Integer shippingId, ShippingForm form) {
        MallShippingExample mse = new MallShippingExample();
        mse.createCriteria().andIdEqualTo(shippingId).andUserIdEqualTo(uid);
        MallShipping ms = new MallShipping();
        BeanUtils.copyProperties(form, ms);
        int row = msm.updateByExampleSelective(ms, mse);
        if (row == 0) ResponseVO.error(ResponseStatusEnum.SHIPPING_NOT_EXIST);
        return ResponseVO.success("更新成功");
    }

    @Override
    public ResponseVO<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        MallShippingExample mse = new MallShippingExample();
        mse.createCriteria().andUserIdEqualTo(uid);
        PageHelper.startPage(pageNum, pageSize);
        List<MallShipping> shippings = msm.selectByExample(mse);
        PageInfo pi = new PageInfo(shippings);
        return ResponseVO.success(pi);
    }
}
